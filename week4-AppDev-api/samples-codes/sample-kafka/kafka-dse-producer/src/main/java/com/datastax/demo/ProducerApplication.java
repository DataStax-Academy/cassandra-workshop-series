package com.datastax.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/** Main class for CannysEngine. */
@SpringBootApplication
@ComponentScan(basePackages = "com.datastax.demo")
@EnableAutoConfiguration
@EnableAdminServer
public class ProducerApplication {

  /** A main method to start this application. */
  public static void main(String[] args) {
    SpringApplication.run(ProducerApplication.class, args);
  }
}
