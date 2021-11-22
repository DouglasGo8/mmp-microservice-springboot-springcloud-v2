package com.packtpub.microservices.springboot.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dougdb
 */
@Slf4j
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub")
public class ProductServiceApp {


  public static void main(String... args) {

    var ctx = SpringApplication.run(ProductServiceApp.class, args);

    var mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
    var mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");

    log.info("Connected to MongoDB {}:{}", mongoDbHost, mongoDbPort);

  }
}
