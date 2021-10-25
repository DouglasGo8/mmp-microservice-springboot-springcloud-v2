package com.packtpub.microservices.springboot.product.composite.mediation.beans.product;

import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.composite.mediation.dto.ProductAggregateDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductServiceClientBean {


  private final String productServicePort;
  private final String productServiceHost;
  private final RestTemplate restTemplate;

  @Autowired
  public ProductServiceClientBean(
          final RestTemplate restTemplate,
          @Value("${app.product-service.port}") String productServicePort,
          @Value("${app.product-service.host}") String productServiceHost) {
    this.restTemplate = restTemplate;
    //
    this.productServicePort = productServicePort;
    this.productServiceHost = productServiceHost;
  }

  @Handler
  @SneakyThrows
  public ProductAggregateDto getProduct(final @Body int productId) {

    final var productServiceUrl = String.format("http://" + this.productServiceHost + ":" +
            this.productServicePort + "/product/%d", productId);

    log.info("Will call getProduct API on URL: {}", productServiceUrl);
    var product = this.restTemplate.getForObject(productServiceUrl, Product.class);
    return new ProductAggregateDto(product);
  }
}
