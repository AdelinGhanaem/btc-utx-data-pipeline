package com.btc.transactions.reader.btctransactionsreader.serder;

import com.btc.transactions.reader.btctransactionsreader.model.UTX;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerDer {


    public byte[] serialize(UTX utx) {
        Schema schema = ReflectData.AllowNull.get().getSchema(UTX.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            DatumWriter<UTX> writer = new ReflectDatumWriter<>(schema);
            writer.write(utx, encoder);
            encoder.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }


    public UTX deserialize(byte[] b) {
        Schema schema = ReflectData.AllowNull.get().getSchema(UTX.class);

        Decoder decoder = DecoderFactory.get().binaryDecoder(b, null);
        DatumReader<UTX> reader = new ReflectDatumReader<>(schema);

        UTX utx = null;
        try {
            utx = reader.read(null, decoder);
            return utx;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
