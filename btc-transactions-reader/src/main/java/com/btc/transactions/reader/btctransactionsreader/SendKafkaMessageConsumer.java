package com.btc.transactions.reader.btctransactionsreader;

import com.btc.transactions.reader.btctransactionsreader.model.UTX;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

import static com.btc.transactions.reader.btctransactionsreader.model.JsonToInstance.jsonToInstance;

@Service
public class SendKafkaMessageConsumer implements Consumer<String> {

    @Autowired
    private KafkaTemplate<String, byte[]> kafka;

    private static final String topic = "utx";



    @Override
    public void accept(String message) {

        UTX utx = jsonToInstance(UTX.class, message);

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
        byte[] serializedBytes = out.toByteArray();
        kafka.send(topic, serializedBytes);
    }


//    Caused by: java.lang.NullPointerException:
//    in com.btc.transactions.reader.btctransactionsreader.model.UTX
//    in com.btc.transactions.reader.btctransactionsreader.model.X
//    in array in com.btc.transactions.reader.btctransactionsreader.model.Out
//    in string null of string in field addr of com.btc.transactions.reader.btctransactionsreader.model.Out
//    of array in field out of com.btc.transactions.reader.btctransactionsreader.model.X
//    in field x of com.btc.transactions.rea
//
//    der.btctransactionsreader.model.UTX

}
