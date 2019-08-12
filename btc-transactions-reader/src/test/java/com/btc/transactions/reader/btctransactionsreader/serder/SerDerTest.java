package com.btc.transactions.reader.btctransactionsreader.serder;

import com.btc.transactions.reader.btctransactionsreader.model.UTX;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerDerTest {


    @Test

    public void serializeDeserializeTest() {
        SerDer serDer = new SerDer();

        byte[] utx = serDer.serialize(new UTX());



    }
}