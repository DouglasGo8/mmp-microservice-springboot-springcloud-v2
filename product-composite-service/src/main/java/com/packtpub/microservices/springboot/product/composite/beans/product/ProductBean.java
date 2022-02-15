package com.packtpub.microservices.springboot.product.composite.beans.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import com.packtpub.microservices.springboot.product.composite.dto.ProductAggregateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductBean extends CommonOpsBean {


  private final String productServicePort;
  private final String productServiceHost;
  private final RestTemplate restTemplate;



  @Autowired
  public ProductBean(
          final RestTemplate restTemplate,
          @Value("${app.product-service.port}") String productServicePort,
          @Value("${app.product-service.host}") String productServiceHost) {

    this.restTemplate = restTemplate;
    //
    this.productServicePort = productServicePort;
    this.productServiceHost = productServiceHost;
  }

  public ProductAggregateDto getProduct(final @Body int productId) {

    final var url = String.format("/product/%d", productId);
    final var productService = this.productServiceUrl(url);

    log.info("Will call getProduct API on URL: {}", productService);
    var product = this.restTemplate.getForObject(productService, Product.class);
    return new ProductAggregateDto(product);
  }


  public void createProduct(final @Body ProductAggregate body) {
    try {
      final var productService = this.productServiceUrl("/product");
      log.info("Will post a new product to URL: {}", productService);
      // var product = this.restTemplate.postForObject(productService, body, Product.class);
      // log.debug("Created a product with id: {}", product.getProductId());
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }


  private String productServiceUrl(String dynamicUri) {
    return "http://" + this.productServiceHost + ":" +
            this.productServicePort + dynamicUri;

  }


}
