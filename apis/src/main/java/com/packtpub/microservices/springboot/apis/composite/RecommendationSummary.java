package com.packtpub.microservices.springboot.apis.composite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationSummary {

  private final int rate;
  private final int recommendationId;

  private final String author;

}
