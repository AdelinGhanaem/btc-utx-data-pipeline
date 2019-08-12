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
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.PrimitiveArrayTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.aggregation.AggregationFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.Properties;

import static btc.da.BTCAmmountTags.tag;
import static btc.da.BTCSatoshi.toBtc;

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

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
// only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2181");
//		properties.setProperty("group.id", "test");
        DataStream<byte[]> utxDs = env.addSource(new FlinkKafkaConsumer("utx", new DeserializationSchema<byte[]>() {
            @Override
            public TypeInformation<byte[]> getProducedType() {
                return PrimitiveArrayTypeInfo.BYTE_PRIMITIVE_ARRAY_TYPE_INFO;
            }

            @Override
            public byte[] deserialize(byte[] message) throws IOException {
                return message;
            }

            @Override
            public boolean isEndOfStream(byte[] nextElement) {
                return nextElement == null;
            }
        }, properties));

        SingleOutputStreamOperator<UTX> utxSingleOutputStreamOperator = utxDs.process(new ProcessFunction<byte[], UTX>() {
            @Override
            public void processElement(byte[] bytes, Context context, Collector<UTX> collector) throws Exception {
                Schema schema = ReflectData.AllowNull.get().getSchema(UTX.class);

                Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
                DatumReader<UTX> reader = new ReflectDatumReader<>(schema);

                UTX utx = reader.read(null, decoder);
                collector.collect(utx);
            }
        }).returns(UTX.class);
        utxSingleOutputStreamOperator.process(new ProcessFunction<UTX, BTCAmountTaggedUTX>() {
            @Override
            public void processElement(UTX value, Context ctx, Collector<BTCAmountTaggedUTX> out) throws Exception {

                if (value.getX() != null && value.getX().getOut() != null && value.getX().getOut().size() > 0) {
                    int tagInt = tag(toBtc(value.getX().getOut().get(0).getValue()));
                    BTCAmountTaggedUTX btcAmountTaggedUTX = new BTCAmountTaggedUTX();
                    btcAmountTaggedUTX.setTag(tagInt);
                    btcAmountTaggedUTX.setUtx(value);
                    out.collect(btcAmountTaggedUTX);
                }
            }
        })
                .keyBy((KeySelector<BTCAmountTaggedUTX, Integer>) BTCAmountTaggedUTX::getTag)
                .timeWindowAll(Time.seconds(5)).aggregate(
                new AggregateFunction<BTCAmountTaggedUTX, Tuple2<String, Long>, Tuple2<String, Long>>() {

                    @Override
                    public Tuple2<String, Long> createAccumulator() {
                        return new Tuple2<>();

                    }

                    @Override
                    public Tuple2<String, Long> add(BTCAmountTaggedUTX value, Tuple2<String, Long> accumulator) {
                        if (accumulator.f1 == null)
                            accumulator.f1 = 1L;
                        else
                            accumulator.f1 = accumulator.f1 + 1L;
                        accumulator.f0 = String.valueOf(value.getTag());
                        return accumulator;
                    }

                    @Override
                    public Tuple2<String, Long> getResult(Tuple2<String, Long> accumulator) {
                        return accumulator;
                    }

                    @Override
                    public Tuple2<String, Long> merge(Tuple2<String, Long> a, Tuple2<String, Long> b) {
                        a.f1 = a.f1 + 1;
                        return a;
                    }
                }).print();


//                .fold(new Tuple2<>("", 0L), new FoldFunction<BTCAmountTaggedUTX, Tuple2<? super String, ? super Long>>() {
//            @Override
//            public Tuple2<? super String, ? super Long> fold(Tuple2<? super String, ? super Long> accumulator, BTCAmountTaggedUTX value) throws Exception {
//                accumulator.f0 = String.valueOf(value.getTag());
//                accumulator.f1 = ((Long) accumulator.f1) + 1L;
//                return accumulator;
//            }
//        }).print();
//                .fold(new Tuple2<>("", 0L), (FoldFunction<BTCAmountTaggedUTX, Tuple2<? super String, ? super Long>>) (accumulator, value) -> {
//                    accumulator.f0 = String.valueOf(value.getTag());
//                    accumulator.f1 = ((Long) accumulator.f1) + 1L;
//                    return accumulator;
//                }).print();
//





        /*
         * Here, you can start creating your execution plan for Flink.
         *
         * Start with getting some data from the environment, like
         * 	env.readTextFile(textPath);
         *
         * then, transform the resulting DataStream<String> using operations
         * like
         * 	.filter()
         * 	.flatMap()
         * 	.join()
         * 	.coGroup()
         *
         * and many more.
         * Have a look at the programming guide for the Java API:
         *
         * http://flink.apache.org/docs/latest/apis/streaming/index.html
         *
         */

        // execute program
        env.execute("BTC Aggregate ... let's see what happens !!! ");
    }

    private static UTX parse(byte[] bytes) {
        return null;
    }


}
