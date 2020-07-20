package com.killrvideo.rest;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages= { "com.killrvideo" })
@EnableAutoConfiguration( exclude = {
        org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration.class
} )
public class KillrVideoRestApi {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KillrVideoRestApi.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
    
}