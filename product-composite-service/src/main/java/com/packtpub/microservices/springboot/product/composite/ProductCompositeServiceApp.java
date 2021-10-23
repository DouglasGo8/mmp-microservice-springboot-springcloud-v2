package com.packtpub.microservices.springboot.product.composite;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @author dougdb
 */
@SpringBootApplication
// apis/utils packages explicit scan
@ComponentScan("com.packtpub.microservices.springboot")
public class ProductCompositeServiceApp {
  public static void main(String... args) {
    SpringApplication.run(ProductCompositeServiceApp.class, args);
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
