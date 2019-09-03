package btc.da;

import btc.da.model.UTX;
import btc.da.model.X;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static btc.da.sink.Util.*;
import static junit.framework.TestCase.fail;

public class PubSubConnectivityTest {


    private Object monitor = new Object();

    @Test
    @RepeatedTest(value = 10)
    public void shouldReceiveWhatWasSent() throws Exception {

        final AtomicBoolean b = new AtomicBoolean(false);

        Publisher publisher = createPublisher("gpapstest", "you_topic", "gpapstest-307ea47d821f.json");

        UTX utx = new UTX();
        X x = new X();
        x.setTime(new Date().getTime());
        x.setHash("some_hash");
        x.setRelayedBy("Relayed On");
        utx.setX(x);

        List<ApiFuture<String>> list = new ArrayList<>();

        final List<String> messages = new ArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            ApiFuture<String> api = writeToPubSub(utx, "gpapstest", "you_topic", publisher);
            api.addListener(() -> {
//                messages.add("message received ... !!!");
            }, MoreExecutors.directExecutor());
        }


        final Subscriber subscriber = createSubscriber(
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    consumer.ack();
                    messages.add(message.getData().toString());
                },
                "gpapstest",
                "your_topic_sub",
                "gpapstest-ac80c480fa24.json");

        assert subscriber != null;
        subscriber.awaitRunning(10, TimeUnit.SECONDS);

        int tries = 0;
        while (messages.size() < 10 && tries < 10) {
            tries++;
            synchronized (this) {
                wait(1000);
            }
        }

        if (messages.size() < 10)
            fail();

    }
}
