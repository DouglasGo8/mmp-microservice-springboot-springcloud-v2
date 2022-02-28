package com.packtpub.microservices.springboot.recommendation.dto;

import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author dougdb
 */
@Slf4j
@Getter
@AllArgsConstructor
public class RecommendationDto {
  private final List<Recommendation> recommendations;
}
