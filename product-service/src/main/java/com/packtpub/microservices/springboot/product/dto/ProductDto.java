package com.packtpub.microservices.springboot.product.dto;

import com.packtpub.microservices.springboot.apis.core.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dougdb
 */
@Getter
@AllArgsConstructor
public class ProductDto {
  private final Product product;
}
