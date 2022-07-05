package com.packtpub.microservices.springboot.review.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * @author dougdb
 */
public interface ReviewRepository extends /*CrudRepository<ReviewEntity, Integer>*/
        ReactiveCrudRepository<ReviewEntity, Integer> {
  @Transactional(readOnly = true)
    //List<ReviewEntity> findByProductId(int productId);
  Flux<ReviewEntity> findByProductId(int productId);
}
