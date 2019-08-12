package com.btc.transactions.reader.btctransactionsreader.model;

import com.google.gson.Gson;

public class JsonToInstance {

    private static final Gson g = new Gson();

    public static <T> T jsonToInstance(Class<T> clazz, String s) {
        return g.fromJson(s, clazz);
    }
}
