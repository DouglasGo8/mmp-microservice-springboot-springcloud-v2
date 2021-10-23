package com.packtpub.microservices.springboot.review;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dougdb
 */
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub.microservices.springboot")
public class ReviewServiceApp {
  public static void main(String... args) {
    SpringApplication.run(ReviewServiceApp.class, args);
  }
}
