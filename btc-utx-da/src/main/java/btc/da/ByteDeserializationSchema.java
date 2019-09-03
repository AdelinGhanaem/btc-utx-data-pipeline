package btc.da;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.PrimitiveArrayTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;

/**
 * Needed for FlinkKafkaConsumer this "DeserializationSchema"
 * passes the bytes as is, as there is a process function that is responsible for converting avro byte format to
 * DTOs.
 */
class ByteDeserializationSchema implements DeserializationSchema<byte[]> {
    @Override
    public TypeInformation<byte[]> getProducedType() {
        return PrimitiveArrayTypeInfo.BYTE_PRIMITIVE_ARRAY_TYPE_INFO;
    }

    @Override
    public byte[] deserialize(byte[] message) {
        return message;
    }
    @Override
    public boolean isEndOfStream(byte[] nextElement) {
        return nextElement == null;
    }
}
