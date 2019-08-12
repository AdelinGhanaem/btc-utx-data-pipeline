package com.btc.transactions.reader.btctransactionsreader;

import com.btc.transactions.reader.btctransactionsreader.model.UTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.btc.transactions.reader.btctransactionsreader.BTCClientWebSocketEndPoint.endPoint;
import static com.btc.transactions.reader.btctransactionsreader.model.JsonToInstance.jsonToInstance;

@Component
public class MainAppRunner {
    //refer to https://www.blockchain.com/api/api_websocket
    public static final String WSS_WS_BLOCKCHAIN_INFO_INV = "wss://ws.blockchain.info/inv";
    private static final int threadsCount = 10;
    public static final String SUBSCRIBE_TO_UTX = "{\"op\":\"unconfirmed_sub\"}";
    private static final String topic = "utx";

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    final static CountDownLatch countDownLatch = new CountDownLatch(threadsCount);

    @Autowired
    SendKafkaMessageConsumer consumer;

    @PostConstruct
    public void run() throws InterruptedException, IOException, URISyntaxException, DeploymentException {

        //create a web socket session and from embedded container, in this case it is Tomcat
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(container.getDefaultMaxTextMessageBufferSize() * 100);
        container.setDefaultMaxBinaryMessageBufferSize(container.getDefaultMaxBinaryMessageBufferSize() * 100);

        Session session = container.connectToServer(endPoint(consumer, e -> {
            countDownLatch.countDown();
        }), getPath());

        //Do we need this shit ?
        session.getBasicRemote().sendText(SUBSCRIBE_TO_UTX);

        //Try and catch InterruptedException !!!!
        countDownLatch.await();
    }


    private static URI getPath() throws URISyntaxException {
        return new URI(WSS_WS_BLOCKCHAIN_INFO_INV);
    }

}
