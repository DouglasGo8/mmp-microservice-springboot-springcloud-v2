package com.packtpub.microservices.springboot.apis.core.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dougdb
 */
@Getter
@Setter
@AllArgsConstructor
public class Product {

  private int productId;
  private int productWeight;
  private String productName;
  private String serviceAddress;

  public Product() {
    productId = 0;
    productName = null;
    productWeight = 0;
    serviceAddress = null;
  }

  public void setServiceAddress(String serviceAddress) {
    this.serviceAddress = serviceAddress;
  }


}
