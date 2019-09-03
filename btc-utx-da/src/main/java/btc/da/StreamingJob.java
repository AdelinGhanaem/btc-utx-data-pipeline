/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package btc.da;

import btc.da.model.BTCAmountTaggedUTX;
import btc.da.model.UTX;
import com.google.gson.Gson;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.Serializable;
import java.util.Properties;

import static btc.da.sink.SinkFunctionsFactory.pubSubSink;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

    public static final int ACCUMULATION_TIME = 5;
    public static final int MAX_DELAY = 10;

    public static final Gson GSON = new Gson();

    public static void main(String[] args) throws Exception {


        if (args.length < 3) {
            System.out.println("Usage java -jar btc-utx-da-0.1 google_project_id topic_id config_file_path");
            return;
        }

        boolean d = false;

        if (args.length > 3)
            d = Boolean.valueOf(args[3]);


        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(3);
        env.setParallelism(10);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2181");
        //properties.setProperty("group.id", "test");

        DataStream<byte[]> utxDs = env.addSource(new FlinkKafkaConsumer<>("utx",
                new ByteDeserializationSchema(), properties));

        DataStream<UTX> utxSingleOutputStreamOperator =
                utxDs.map(new BytesToUTXProcessFunction()).returns(UTX.class);

        SingleOutputStreamOperator<BTCPerTagInTimeWindow> utx =
                utxClusteredByAmountPipeline(utxSingleOutputStreamOperator, ACCUMULATION_TIME, MAX_DELAY, d);


        SingleOutputStreamOperator<String> json =
                utx.map(new RichMapFunction<BTCPerTagInTimeWindow, String>() {

                            private transient Gson gson;

                            @Override
                            public String map(BTCPerTagInTimeWindow value) throws Exception {
                                return gson.toJson(value);
                            }

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                gson = new Gson();
                            }
                        }
                );

        json.addSink(pubSubSink(args[0], args[1], args[2]));

        env.execute("BTC Aggregate ... let's see what happens !");
    }

    public static SingleOutputStreamOperator<BTCPerTagInTimeWindow> utxClusteredByAmountPipeline(DataStream<UTX> dataStream, int accumulationTime, final int seconds, boolean debug) {
        //UTX clustered by amount and timely analysed per 30 seconds time windows
        return dataStream
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<UTX>(Time.seconds(seconds)) {
                    @Override
                    public long extractTimestamp(UTX element) {
                        // convert the element to milliseconds
                        return element.getX().getTime() * 1000;
                    }
                })
                .process(new UTXBTCAmountTaggedUTXProcessFunction(debug))
                .keyBy((KeySelector<BTCAmountTaggedUTX, Integer>) BTCAmountTaggedUTX::getTag)
                .timeWindow(Time.seconds(accumulationTime))
                .process(new UTXAccumulator(debug));
    }


    public static class BTCPerTagInTimeWindow implements Serializable {
        Long startTime = 0L;
        Long endTime = 0L;
        Integer tag;
        Long amount;
        Integer counter;

        public BTCPerTagInTimeWindow(Long startTime,
                                     Long endTime,
                                     Integer tag,
                                     Integer counter, Long amount) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.tag = tag;
            this.counter = counter;
            this.amount = amount;
        }


        public BTCPerTagInTimeWindow() {
        }

        @Override
        public String toString() {
            return "BTCPerTagInTimeWindow{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", tag=" + tag +
                    ", counter=" + counter +
                    '}';
        }
    }

    private static UTX parse(byte[] bytes) {
        return null;
    }


}
