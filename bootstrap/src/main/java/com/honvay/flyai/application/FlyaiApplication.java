package com.honvay.flyai.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.honvay")
public class FlyaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyaiApplication.class,args);

        new WebSocketServer(7800).run();

    }

}
