package com.packtpub.microservices.springboot.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dougdb
 */
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub")
public class ProductServiceApp {
  public static void main(String... args) {
    SpringApplication.run(ProductServiceApp.class, args);
  }
}
