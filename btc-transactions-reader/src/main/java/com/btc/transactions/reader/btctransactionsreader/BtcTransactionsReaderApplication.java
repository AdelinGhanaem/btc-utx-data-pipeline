package com.btc.transactions.reader.btctransactionsreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class BtcTransactionsReaderApplication {
        public static void main(String[] args) throws IOException, DeploymentException, URISyntaxException, InterruptedException {
        SpringApplication.run(BtcTransactionsReaderApplication.class, args);
    }
}
