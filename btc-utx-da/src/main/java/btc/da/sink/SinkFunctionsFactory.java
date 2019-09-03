package btc.da.sink;

import btc.da.seriazlization.SerializationUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.gcp.pubsub.PubSubSink;

import java.io.IOException;
import java.util.Objects;

public class SinkFunctionsFactory {


    public static <T> SinkFunction<T> pubSubSink(String project, String topic, String googleKeyFile) throws IOException {


        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Objects.requireNonNull(SinkFunctionsFactory.class.getClassLoader().getResourceAsStream(
                        googleKeyFile)))
                .createScoped(Lists.newArrayList(
                        "https://www.googleapis.com/auth/cloud-platform"));

        return PubSubSink.newBuilder()
                .<T>withSerializationSchema(new SerializationUtil.AvroSerializationSchema<>())
                .withProjectName(project)
                .withTopicName(topic)
                .withCredentials(credentials)
                .build();
    }
}
