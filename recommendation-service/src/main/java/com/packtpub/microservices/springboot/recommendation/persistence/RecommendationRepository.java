package com.packtpub.microservices.springboot.recommendation.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author dougdb
 */
public interface RecommendationRepository extends CrudRepository<RecommendationEntity, String> {
  List<RecommendationEntity> findByProductId(int productId);
}