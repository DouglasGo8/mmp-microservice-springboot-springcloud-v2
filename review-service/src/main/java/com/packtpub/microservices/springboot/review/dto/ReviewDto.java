package com.packtpub.microservices.springboot.review.dto;

import com.packtpub.microservices.springboot.apis.core.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author dougdb
 */
@Getter
@AllArgsConstructor
public class ReviewDto {
  private final List<Review> reviews;
}
