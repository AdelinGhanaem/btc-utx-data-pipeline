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

import btc.da.model.UTX;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.runtime.io.network.DataExchangeModeTest;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

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

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(10);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2181");
        //properties.setProperty("group.id", "test");

        DataStream<byte[]> utxDs = env.addSource(new FlinkKafkaConsumer<>("utx", new ByteDeserializationSchema(), properties));

        DataStream<UTX> utxSingleOutputStreamOperator = utxDs.map(new BytesToUTXProcessFunction()).returns(UTX.class);


        //UTX clustered by amount and timely analysed per 30 seconds time windows
        SingleOutputStreamOperator<BTCPerTagInTimeWindow> utx =
                utxSingleOutputStreamOperator
                        .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<UTX>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(UTX element) {
                                return element.getX().getTime() * 1000;
                            }
                        })

                        .process(new UTXBTCAmountTaggedUTXProcessFunction())

                        .keyBy((KeySelector<BTCAmountTaggedUTX, Integer>) BTCAmountTaggedUTX::getTag)

                        .timeWindow(Time.seconds(30))

                        .process(new ProcessWindowFunction<BTCAmountTaggedUTX, BTCPerTagInTimeWindow, Integer, TimeWindow>() {
                            @Override
                            public void process(Integer integer,
                                                Context context,
                                                Iterable<BTCAmountTaggedUTX> elements,
                                                Collector<BTCPerTagInTimeWindow> out) {
                                System.out.print("Function process fires at " + new Date());
                                Integer i = 0;

                                for (BTCAmountTaggedUTX u : elements) {
                                    i++;
                                }

                                BTCPerTagInTimeWindow b = new BTCPerTagInTimeWindow(context.window().getStart(), context.window().getEnd(), integer, i);
                                out.collect(b);
                            }
                        });

//        .print();
        // execute program
        env.execute("BTC Aggregate ... let's see what happens !!! ");
    }


    public static class BTCPerTagInTimeWindow {
        Long startTime = 0L;
        Long endTime = 0L;
        Integer tag;
        Integer counter;


        public BTCPerTagInTimeWindow(Long startTime, Long endTime, Integer tag, Integer counter) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.tag = tag;
            this.counter = counter;
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
