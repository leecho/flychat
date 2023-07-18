package com.honvay.flychat.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.honvay")
public class FlychatApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlychatApplication.class,args);
    }

}
