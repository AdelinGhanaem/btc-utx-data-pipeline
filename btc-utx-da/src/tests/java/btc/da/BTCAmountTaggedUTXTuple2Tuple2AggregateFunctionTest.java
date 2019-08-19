package btc.da;

import btc.da.model.UTX;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BTCAmountTaggedUTXTuple2Tuple2AggregateFunctionTest {


    @Test
    @RepeatedTest(value = 1)
    public void aggregate() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setBufferTimeout(0);
        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);

        CollectSink.values.clear();
        SingleOutputStreamOperator<BTCAmountTaggedUTX> ds =

                // here we can play with time stamps and watermarks ...
//                env.fromElements(create(1), create(2), create(2), create(2), create(3)).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<BTCAmountTaggedUTX>() {
//
//                    @Nullable
//                    @Override
//                    public Watermark getCurrentWatermark() {
//                        return new Watermark(System.currentTimeMillis());
//                    }
//
//                    @Override
//                    public long extractTimestamp(BTCAmountTaggedUTX element, long previousElementTimestamp) {
//                        return System.currentTimeMillis();
//                    }
//                });
                env.fromElements(
                        create(1),
                        create(2),
                        create(2),
                        create(2),
                        create(3));

        ds.keyBy((KeySelector<BTCAmountTaggedUTX, Integer>) BTCAmountTaggedUTX::getTag)
                .timeWindowAll(Time.seconds(1))
                .aggregate(new MapAccumulator())
                .addSink(new CollectSink());

        ds.writeAsText("./result.txt", FileSystem.WriteMode.OVERWRITE);

        env.execute();
//        Thread.sleep(2000);
        assert !CollectSink.values.isEmpty();

        Map<String, Integer> integerMap = CollectSink.values.get(0);
        assert integerMap.get(String.valueOf(1)) == 1;
        assert integerMap.get(String.valueOf(2)) == 3;
        assert integerMap.get(String.valueOf(3)) == 1;
        CollectSink.values.forEach(System.out::println);

    }


    public static class MockFlinkSource implements SourceFunction<BTCAmountTaggedUTX> {

        @Override
        public void run(SourceContext<BTCAmountTaggedUTX> ctx) throws Exception {

            ctx.collectWithTimestamp(create(1, now(1)), now(1));
            long t = now(1);
            ctx.collectWithTimestamp(create(1, now(1)), now(1));

            ctx.collectWithTimestamp(create(2, now(2)), now(2));

            ctx.collectWithTimestamp(create(2, now(2)), now(2));

            ctx.collectWithTimestamp(create(3, now(3)), now(3));

            ctx.collectWithTimestamp(create(4, now(4)), now(3));

            // this element should not be included as it is late with 3 seconds and the water
            // mark is emitted every 2 seconds
            Thread.sleep(3000);
            ctx.collectWithTimestamp(create(1, t), t);

        }

        @Override
        public void cancel() {

        }
    }

    static List<List<Integer>> s = new ArrayList<>();

    @Test
    public void playingWithWatermarksAndTime() throws Exception {


        /**
         * We will generate these elements .... [ 1,2,3,4,5 .... ]
         *
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // Either add source with calling collectWithTimeStamp ... or use assignTimestampsAndWatermarks
        DataStreamSource<BTCAmountTaggedUTX> ds = env.addSource(new MockFlinkSource());

//        DataStreamSource<BTCAmountTaggedUTX> ds = env.fromElements(
//
//                create(1, now(1)),
//
//                create(1, now(1)),
//
//                create(2, now(2)),
//                create(2, now(2)),
//
//                create(3, now(3)),
//                create(3, now(3))
//
//        );

        SingleOutputStreamOperator<BTCAmountTaggedUTX> withTime =
                ds.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<BTCAmountTaggedUTX>() {
                    Long lastTime = now(0);

                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {

                        long currentTime = now(0);
                        // every 2 seconds emit a new water mark ...
                        if (currentTime - lastTime >= (2 * 1000)) {
                            return new Watermark(currentTime);
                        }
                        // null indicate now new water mark ....
                        return null;
                    }

                    @Override
                    public long extractTimestamp(BTCAmountTaggedUTX element, long previousElementTimestamp) {
                        return element.getTime();
                    }
                });


//        KeyedStream<BTCAmountTaggedUTX, Integer> ds1 = withTime.
//                keyBy((KeySelector<BTCAmountTaggedUTX, Integer>) BTCAmountTaggedUTX::getTag);

        withTime.timeWindowAll(Time.seconds(1)).process(new ProcessAllWindowFunction<BTCAmountTaggedUTX, List<Integer>, TimeWindow>() {
            @Override
            public void process(Context context, Iterable<BTCAmountTaggedUTX> elements, Collector<List<Integer>> out) {
//
                System.out.println("Process is called on: " + toTime(System.currentTimeMillis()));


//                System.out.println("Time window start : " + toTime(context.window().getStart()));
//                System.out.println("Time window end: " + toTime(context.window().getEnd()));

                System.out.println("Time window start : " + toTime(context.window().getStart()));
                System.out.println("Time window end: " + toTime(context.window().getEnd()));
                for (BTCAmountTaggedUTX u : elements) {
                    System.out.println(u.getTag());
                }

                System.out.println(" ^^^^^^^^^^^^^^^^^^^^^^^^^ ");


            }
        })

//                .process(collectAndSort())
                .addSink(getSinkFunction());

        env.execute();

        assert !s.isEmpty();
        System.out.println("Size: " + s.size());

        CollectSink.values.forEach(System.out::println);
    }

    private static long now(int seconds) {

        long l = new Date().getTime();
        l = l + (seconds * 1000);
        return l;
    }

    private ProcessWindowFunction<BTCAmountTaggedUTX, List<Integer>, Integer, TimeWindow> collectAndSort() {
        return new ProcessWindowFunction<BTCAmountTaggedUTX, List<Integer>, Integer, TimeWindow>() {
            @Override
            public void process(Integer integer, Context context, Iterable<BTCAmountTaggedUTX> elements, Collector<List<Integer>> out) throws Exception {
                List<Integer> l = StreamSupport.stream(elements.spliterator(), false)
                        .map(BTCAmountTaggedUTX::getTag).collect(Collectors.toList());

                Collections.sort(l);

                out.collect(l);
            }
        };
    }

    private SinkFunction<List<Integer>> getSinkFunction() {
        return new SinkFunction<List<Integer>>() {
            @Override
            public void invoke(List<Integer> value, Context context) throws Exception {
                s.add(value);
                value.forEach(System.out::println);
            }
        };
    }


    private static LocalDateTime toTime(long i) {
        Instant in = Instant.ofEpochMilli(i);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(in, ZoneId.systemDefault());
        return localDateTime;
    }

    // create a testing sink
    private static class CollectSink implements SinkFunction<Map<String, Integer>> {

        // must be static
        public static final List<Map<String, Integer>> values = new ArrayList<>();

        @Override
        public void invoke(Map<String, Integer> value, Context context) throws Exception {
            values.add(value);
        }
    }


    public static class Timex extends BoundedOutOfOrdernessTimestampExtractor {

        public Timex(Time maxOutOfOrderness) {
            super(maxOutOfOrderness);
        }

        @Override
        public long extractTimestamp(Object element) {
            return 0;
        }
    }

    private static BTCAmountTaggedUTX create(int tag, long time) {
        BTCAmountTaggedUTX utx = new BTCAmountTaggedUTX();
        utx.setTag(tag);
        utx.setTime(time);
        UTX utx1 = new UTX();
        utx.setUtx(utx1);
        return utx;
    }

    private static BTCAmountTaggedUTX create(int tag) {
        return create(tag, 0);
    }
}