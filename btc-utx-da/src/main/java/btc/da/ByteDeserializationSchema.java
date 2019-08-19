package btc.da;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.PrimitiveArrayTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

class ByteDeserializationSchema implements DeserializationSchema<byte[]> {
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
}
