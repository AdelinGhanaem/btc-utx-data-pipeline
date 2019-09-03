package btc.da.seriazlization;

import btc.da.model.UTX;
import btc.da.model.X;
import com.google.protobuf.ByteString;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.junit.jupiter.api.Test;

import java.util.Date;

class SerializationUtilTest {


    @Test
    public void playAroundWithAvro() {

        UTX utx = new UTX();
        X x = new X();
        x.setTime(new Date().getTime());
        x.setHash("some_hash");
        x.setRelayedBy("Relayed On");
        utx.setX(x);


        SerializationSchema s = new SerializationUtil.AvroSerializationSchema();

        byte[] bytes = s.serialize(utx);

        ByteString b = ByteString.copyFrom(bytes);

//        System.out.print(b.);


    }

}