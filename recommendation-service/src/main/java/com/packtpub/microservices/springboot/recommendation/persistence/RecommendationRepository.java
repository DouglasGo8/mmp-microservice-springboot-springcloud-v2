package com.packtpub.microservices.springboot.recommendation.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author dougdb
 */
public interface RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity, String>
        /*CrudRepository<RecommendationEntity, String>*/ {
  //List<RecommendationEntity> findByProductId(int productId);
  Flux<RecommendationEntity> findByProductId(int productId);
}
