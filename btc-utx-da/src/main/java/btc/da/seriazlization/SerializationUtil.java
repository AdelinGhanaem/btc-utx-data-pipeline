package btc.da.seriazlization;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.flink.api.common.serialization.SerializationSchema;

import java.io.ByteArrayOutputStream;

public class SerializationUtil {


    public static class AvroSerializationSchema<T> implements SerializationSchema<T> {
        @Override
        public byte[] serialize(T element) {
            Schema schema = ReflectData.AllowNull.get().getSchema(element.getClass());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                DatumWriter<T> writer = new ReflectDatumWriter<>(schema);
                writer.write(element, encoder);
                encoder.flush();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return out.toByteArray();
        }
    }
}
