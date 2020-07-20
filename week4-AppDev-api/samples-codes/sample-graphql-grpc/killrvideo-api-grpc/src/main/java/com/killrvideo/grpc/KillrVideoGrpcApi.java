package com.killrvideo.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages="com.killrvideo")
@EnableAutoConfiguration
public class KillrVideoGrpcApi {

    /**
     * As SpringBoot application, this is the "main" class
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KillrVideoGrpcApi.class);
        app.run(args);
    }
    
}