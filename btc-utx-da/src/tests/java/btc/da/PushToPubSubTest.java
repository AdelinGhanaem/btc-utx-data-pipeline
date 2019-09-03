package btc.da;

import btc.da.model.Out;
import btc.da.model.UTX;
import btc.da.model.X;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.util.Collector;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static btc.da.StreamingJob.utxClusteredByAmountPipeline;
import static btc.da.sink.SinkFunctionsFactory.pubSubSink;

public class PushToPubSubTest {


    public static final String TOPIC = "your_topic_sub";
    public static final String PROJECT = "gpapstest";

    @Test
    public void pushingToPubSubTest() throws Exception {


        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<UTX> utxSingleOutputStreamOperator = env.fromCollection(Lists.newArrayList(
                utx(now()),
                utx(now()),
                utx(now()),
                utx(now())
        ));

        SingleOutputStreamOperator<StreamingJob.BTCPerTagInTimeWindow> utx =
                utxClusteredByAmountPipeline(utxSingleOutputStreamOperator, 1, 1, true);
        utx.addSink(new SinkFunction<StreamingJob.BTCPerTagInTimeWindow>() {
            @Override
            public void invoke(StreamingJob.BTCPerTagInTimeWindow value, Context context) throws Exception {
                System.out.println("Sent: " + value);
            }
        });

        utx.process(new ProcessFunction<StreamingJob.BTCPerTagInTimeWindow, String>() {
            @Override
            public void processElement(StreamingJob.BTCPerTagInTimeWindow value, Context ctx, Collector<String> out) {
                Gson g = new Gson();
                out.collect(g.toJson(value));
            }
        }).addSink(pubSubSink(PROJECT, TOPIC, "gpapstest-307ea47d821f.json"));

        AtomicBoolean b = new AtomicBoolean(false);
        env.execute("BTC Aggregate ... let's see what happens !!! ");
//        readFromPubSub(2, (message, consumer) -> {
//            consumer.ack();
//            b.set(true);
//            System.out.print("Received: " + message);
//        }, PROJECT, TOPIC, "gpapstest-ac80c480fa24.json");


        assert b.get();
    }

    private long now() {
        return (new Date().getTime()) / 1000;
    }

    private UTX utx(long time) {
        UTX u = new UTX();
        X x = new X();
        x.setRelayedBy("AlaBala 2 Chuvala !!! ");
        x.setTime(time);
        x.setTxIndex(1000L);
        x.setHash("HashHAHAHAHHAAHHA");
        Out out = new Out();
        out.setAddr("add");
        out.setSpent(true);
        out.setValue(100L);
        x.setOut(Lists.newArrayList(out, out, out));
        u.setX(x);
        return u;
    }
}





