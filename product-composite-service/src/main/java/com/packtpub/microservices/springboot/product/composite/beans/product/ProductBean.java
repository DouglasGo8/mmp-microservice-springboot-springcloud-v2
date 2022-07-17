package com.packtpub.microservices.springboot.product.composite.beans.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductBean extends CommonOpsBean {

  private final ObjectMapper mapper;
  private final WebClient webClient;
  private final String productServicePort;
  private final String productServiceHost;
  //private final RestTemplate restTemplate;


  @Autowired
  public ProductBean(
          /*final RestTemplate restTemplate,*/
          ObjectMapper mapper,
          WebClient.Builder webClient,
          @Value("${app.product-service.port}") String productServicePort,
          @Value("${app.product-service.host}") String productServiceHost) {
    //
    //this.restTemplate = restTemplate;
    //
    this.mapper = mapper;
    this.webClient = webClient.build();
    this.productServicePort = productServicePort;
    this.productServiceHost = productServiceHost;
  }

  public /*ProductAggregateDto*/ Mono<Product> getProduct(final /*@Body*/ int productId) {

    final var partialUrl = String.format("/product/%d", productId);
    final var productServiceUri = this.productServiceUrl(partialUrl);

    log.info("Will call getProduct API on URL: {}", productServiceUri);
    // var product = this.restTemplate.getForObject(productService, Product.class);
    return this.webClient.get().uri(productServiceUri)
            .retrieve().bodyToMono(Product.class)
            .log(log.getName(), Level.FINE)
            .onErrorMap(WebClientResponseException.class, super::handleHttpClientException);

    // return new ProductAggregateDto(product);
  }

  public void createProduct(final @Body ProductAggregate body) {
    try {
      final var productService = this.productServiceUrl("/product");
      final var productToInsert = new Product(body.getProductId(), body.getProductWeight(), body.getProductName(), null);
      log.info("Will post a new product to URL: {}", productService);
      // var product = this.restTemplate.postForObject(productService, productToInsert, Product.class);
      // assert product != null;
      // log.debug("Created a product with id: {}", product.getProductId());
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }

  public void deleteProduct(final @Body int productId) {
    try {
      final var productService = this.productServiceUrl("/product/" + productId);
      log.info("Will post a new product to URL: {}", productService);
      // this.restTemplate.delete(productService);
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }

  private String productServiceUrl(String dynamicUri) {
    return "http://" + this.productServiceHost + ":" + this.productServicePort + dynamicUri;
  }


}
