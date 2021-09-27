package com.packtpub.microservices.springboot.api.composite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewSummary {

  private final int reviewId;

  private final String author;
  private final String subject;
}
