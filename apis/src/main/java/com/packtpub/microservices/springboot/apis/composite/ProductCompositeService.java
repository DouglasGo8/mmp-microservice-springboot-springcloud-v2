package com.packtpub.microservices.springboot.apis.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author dougdb
 */
public interface ProductCompositeService {

  @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
  ProductAggregate getProduct(@PathVariable int productId);
}
