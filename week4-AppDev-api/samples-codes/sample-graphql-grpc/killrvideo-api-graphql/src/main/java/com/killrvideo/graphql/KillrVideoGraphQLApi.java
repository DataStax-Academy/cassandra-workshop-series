package com.killrvideo.graphql;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages="com.killrvideo")
@EnableAutoConfiguration
public class KillrVideoGraphQLApi {

    /**
     * As SpringBoot application, this is the "main" class
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KillrVideoGraphQLApi.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
    
}