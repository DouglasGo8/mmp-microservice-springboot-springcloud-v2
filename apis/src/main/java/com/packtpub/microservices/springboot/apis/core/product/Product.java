package com.packtpub.microservices.springboot.apis.core.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

  private final int productId;
  private final int productWeight;
  private final String productName;
  private final String serviceAddress;

  public Product() {
    productId = 0;
    productName = null;
    productWeight = 0;
    serviceAddress = null;
  }


}
