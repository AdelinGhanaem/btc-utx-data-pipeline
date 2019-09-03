package btc.da;

import btc.da.model.UTX;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.flink.api.common.functions.MapFunction;

class BytesToUTXProcessFunction implements MapFunction<byte[], UTX> {

    @Override
    public UTX map(byte[] value) throws Exception {
        Schema schema = ReflectData.AllowNull.get().getSchema(UTX.class);
        Decoder decoder = DecoderFactory.get().binaryDecoder(value, null);
        DatumReader<UTX> reader = new ReflectDatumReader<>(schema);
        return reader.read(null, decoder);
    }
}
