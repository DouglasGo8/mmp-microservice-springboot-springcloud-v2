package com.packtpub.microservices.springboot.apis.core.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dougdb
 */
@Getter
@Setter
@AllArgsConstructor
public class Recommendation {

  private int rate;
  private int productId;
  private int recommendationId;

  private String author;
  private String content;
  private String serviceAddress;

  public Recommendation() {
    productId = 0;
    recommendationId = 0;
    author = null;
    rate = 0;
    content = null;
    serviceAddress = null;
  }
}
