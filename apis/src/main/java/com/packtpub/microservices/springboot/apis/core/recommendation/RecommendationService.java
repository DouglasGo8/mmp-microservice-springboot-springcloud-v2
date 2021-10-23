package com.packtpub.microservices.springboot.apis.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dougdb
 */
public interface RecommendationService {
  @GetMapping(value = "/recommendation", produces = "application/json")
  List<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
