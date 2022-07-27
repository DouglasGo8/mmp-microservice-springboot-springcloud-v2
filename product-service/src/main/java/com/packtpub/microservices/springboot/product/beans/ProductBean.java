package com.packtpub.microservices.springboot.product.beans;

import com.mongodb.DuplicateKeyException;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.product.persistence.ProductRepository;
import com.packtpub.microservices.springboot.product.services.ProductMapper;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductBean {

  private final ProductMapper mapper;
  private final ServiceUtil serviceUtil;

  private final ProductRepository repository;

  @Autowired
  public ProductBean(ProductRepository repository, ProductMapper mapper,
                     ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
    this.mapper = mapper;
    this.repository = repository;
  }

  /**
   * @param productId id of Product
   * @return mapped ProductDto
   */
  public Mono<Product> getProductById(/*@Body*/ int productId) {

    // var product = new Product(productId, 123, "name-" + productId, serviceUtil.getServiceAddress());

    log.info("getProductById fired with {} param", productId);

    //var entity = this.repository.findByProductId(productId)
    //        .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));\
    //var response = mapper.entityToApi(entity);
    //response.setServiceAddress(serviceUtil.getServiceAddress());
    //return new ProductDto(response);
    return this.repository.findByProductId(productId)
            .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
            .log(log.getName(), Level.FINE)
            .map(mapper::entityToApi)
            .map(this::setServiceAddress);
            //.map(ProductDto::new)
            //.map(ProductDto::getProduct);
    //return  null;
  }

  /**
   * @param product to be Saved
   * @return mapped ProductDto
   */
  public /*ProductDto*/ Mono<Product> createProduct(@Body Product product) {

    log.info("createProduct fired_2={}", product);
    //
    try {
      var entity = mapper.apiToEntity(product);
      return this.repository.save(entity)
              .log(log.getName(), Level.FINE)
              .onErrorMap(DuplicateKeyException.class,
                      ex -> new InvalidInputException("Duplicate key, Product Id: " + product.getProductId()))
              .map(mapper::entityToApi);
              //.map(ProductDto::new)
              //.map(ProductDto::getProduct);
      // return new ProductDto(mapper.entityToApi(newEntity));
      //return null;
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, Product Id" + product.getProductId());
    }
  }

  /**
   * @param productId to be deleted
   */
  public /*void*/ Mono<Void> deleteProduct(/*@Body*/ int productId) {
    log.info("deleteProduct: tries to delete an entity with productId: {}", productId);
    //this.repository.findByProductId(productId).ifPresent(repository::delete);
    return this.repository.findByProductId(productId)
            .log(log.getName(), Level.FINE)
            .map(this.repository::delete)
            .flatMap(e -> e);
  }

  private Product setServiceAddress(Product e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
