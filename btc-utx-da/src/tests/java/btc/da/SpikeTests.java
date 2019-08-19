package btc.da;

import org.apache.commons.collections.collection.SynchronizedCollection;
import org.apache.commons.collections.list.SynchronizedList;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.test.util.AbstractTestBase;
import org.apache.flink.util.Collector;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SpikeTests extends AbstractTestBase {

    static final List<String> s = SynchronizedList.decorate(new ArrayList());

    @Test
//refer to https://training.ververica.com/lessons/connected-streams.html
    void connectedStreams() throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> control = env.fromElements("DROP", "IGNORE", "bla", "bla2").keyBy(x -> x);
        DataStream<String> streamOfWords = env.fromElements("data", "DROP", "artisans", "IGNORE").keyBy(x -> x);

        control
                .connect(streamOfWords)
                .flatMap(new ControlFunction(s))
                .print();

        env.execute();

        s.forEach(System.out::println);
        s.clear();
    }

    public static class ControlFunction extends RichCoFlatMapFunction<String, String, String> {

        private ValueState<Boolean> blocked;
        private List<String> stringList;

        public ControlFunction(List<String> stringList) {
            this.stringList = stringList;
        }

        @Override
        public void open(Configuration config) {
            blocked = getRuntimeContext().getState(new ValueStateDescriptor<>("blocked", Boolean.class));
        }

        @Override
        public void flatMap1(String control_value, Collector<String> out) throws Exception {
//            stringList.add("f1: " + control_value);
            System.out.println("f1: " + control_value);
            blocked.update(Boolean.TRUE);
        }

        @Override
        public void flatMap2(String data_value, Collector<String> out) throws Exception {
//            stringList.add("f2: " + data_value);
            System.out.println("f2: " + data_value);
            if (blocked.value() == null) {
                out.collect(data_value);
            }
        }
    }


    @Test
    void name() {
        long l = 1566224634L * 1000;
        long l2= 1523265822618L;
        Instant i = Instant.ofEpochMilli(l);
        System.out.println(LocalDateTime.ofInstant(i, ZoneId.systemDefault()));
//                System.out.println(new Date(element.getX().getTime()));
        System.out.println();
    }
}
