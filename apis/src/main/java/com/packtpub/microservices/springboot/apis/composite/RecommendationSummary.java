package com.packtpub.microservices.springboot.apis.composite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationSummary {

  private final int rate;
  private final int recommendationId;
  private final String author;
  private final String content;

  public RecommendationSummary() {
    this.recommendationId = 0;
    this.author = null;
    this.rate = 0;
    this.content = null;
  }

}
