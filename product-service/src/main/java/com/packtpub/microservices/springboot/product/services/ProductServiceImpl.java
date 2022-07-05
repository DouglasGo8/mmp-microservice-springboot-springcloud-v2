package com.packtpub.microservices.springboot.product.services;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.product.ProductService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.product.beans.ProductBean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductBean productBean;
  // private final ProducerTemplate producerTemplate;
  //
  // private final CamelReactiveStreamsService crss;

  @Override
  public /*Product*/ Mono<Product> createProduct(Product product) {
    log.info("createProduct fired_1={}", product.getProductId());

    // var dto = this.producerTemplate
    //        .requestBody("{{direct.product.routing.createProduct.endpoint}}", body, Mono.class);
    //return dto.getProduct();
    //var monoProduct = (Mono<ProductDto>) dto;
    // monoProduct.map(ProductDto::getProduct);
    if (product.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + product.getProductId());
    }

    return this.productBean.createProduct(product);
  }

  @Override
  public /*void*/ Mono<Void> deleteProduct(int productId) {
    //this.producerTemplate.asyncRequestBody("{{direct.product.routing.deleteProduct.endpoint}}", productId);
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
    //
    return this.productBean.deleteProduct(productId);
  }

  @Override
  public /*Product*/ Mono<Product> getProduct(int productId) {

    log.debug("/product return the found product for productId={}", productId);

    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);

    //if (productId == 13)
    //  throw new NotFoundException("No product found for productId: " + productId);
    // this.producerTemplate.asyncSendBody("{{direct.product.routing.getProductById.endpoint}}", productId);
    // var fromCamelReactiveToMono = this.crss.from("direct:words", Product.class);

    //return this.productBean.getProductById(productId);
    //return dto.map(ProductDto::getProduct);
    //return dto.getProduct();
    //return Mono.from(fromCamelReactiveToMono);

    return this.productBean.getProductById(productId);
  }


}
