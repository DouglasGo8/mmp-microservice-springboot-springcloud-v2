package com.packtpub.microservices.springboot.apis.composite;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author dougdb
 */
@Getter
@AllArgsConstructor
public class ProductAggregate {

  private final int productId;
  private final int productWeight;
  private final String productName;

  private final List<RecommendationSummary> recommendations;
  private final List<ReviewSummary> reviews;
  private final ServiceAddresses serviceAddresses;
}
