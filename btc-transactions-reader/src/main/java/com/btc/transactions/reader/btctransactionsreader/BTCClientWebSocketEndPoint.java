package com.btc.transactions.reader.btctransactionsreader;

import javax.websocket.*;
import java.util.function.Consumer;


@ClientEndpoint
public class BTCClientWebSocketEndPoint extends Endpoint {

    private Session session;
    private Consumer<String> consumer;
    private Consumer<CloseReason> onCloseCallBack;

    private BTCClientWebSocketEndPoint(Consumer<String> consumer,
                                       Consumer<CloseReason> onCloseCallBack) {
        this.consumer = consumer;
        this.onCloseCallBack = onCloseCallBack;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        consumer.accept(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        if (onCloseCallBack != null)
            onCloseCallBack.accept(closeReason);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public static final BTCClientWebSocketEndPoint endPoint(Consumer<String> consumer,
                                                            Consumer<CloseReason> onCloseCallBack) {
        return new BTCClientWebSocketEndPoint(consumer, onCloseCallBack);

    }


}
