package btc.da.sink;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Topic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Util Used to create subscribers and published to pub/sub
 * checkout the examples here:
 * https://github.com/googleapis/google-cloud-java/blob/master/google-cloud-examples/src/main/java/com/google/cloud/examples/pubsub/snippets/SubscriberSnippets.java
 * https://github.com/googleapis/google-cloud-java/blob/master/google-cloud-examples/src/main/java/com/google/cloud/examples/pubsub/snippets/CreateTopicAndPublishMessages.java
 */
public class Util {

    private static final Gson GSON = new Gson();

    public static Subscriber createSubscriber(MessageReceiver receiver,
                                              String project,
                                              String projectSubscriptionName,
                                              String configFile) {

        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(project, projectSubscriptionName);
        try {
            final GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(Objects.requireNonNull(
                            SinkFunctionsFactory.class.getClassLoader().getResource(
                                    //                                    "gpapstest-ac80c480fa24.json")).getFile()))
                                    configFile)).getFile()))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

            Subscriber subscriber = null;
            // Create a subscriber for "my-subscription-id" bound to the message receiver
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).setCredentialsProvider(() -> credentials)
                    .build();
            subscriber.addListener(new Subscriber.Listener() {
                @Override
                public void failed(ApiService.State from, Throwable failure) {
                    throw new RuntimeException(failure);
                }
            }, MoreExecutors.directExecutor());
            return subscriber;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> ApiFuture<String> writeToPubSub(T t, String project, String topic, Publisher publisher) {
        // Create a publisher instance with default settings bound to the topic
        String json = GSON.toJson(t);
        // schedule publishing one message at a time : messages get automatically batched
        ByteString data = ByteString.copyFromUtf8(json);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        // Once published, returns a server-assigned message id (unique within the topic)
        return publisher.publish(pubsubMessage);
    }

    public static Publisher createPublisher(String project, String topic, String credentialsFileConfig) {
        // [START pubsub_publish]
        ProjectTopicName topicName = ProjectTopicName.of(project, topic);
        Publisher publisher = null;
        try {

            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(Objects.requireNonNull(
                            SinkFunctionsFactory.class.getClassLoader().getResource(
//                                    "gpapstest-ac80c480fa24.json")).getFile()))
                                    credentialsFileConfig)).getFile()))

                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).setCredentialsProvider(() -> credentials).build();

        } catch (IOException e) {

        } finally {

        }
        return publisher;
    }

    public static Topic createTopic(String project, String topic1) throws Exception {
        ProjectTopicName topic = ProjectTopicName.of(project, topic1);
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            return topicAdminClient.createTopic(topic);
        }
    }

}
