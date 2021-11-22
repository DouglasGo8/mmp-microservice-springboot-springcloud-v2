package com.packtpub.microservices.springboot.review.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dougdb
 */
public interface ReviewRepository extends CrudRepository<ReviewEntity, Integer> {
  @Transactional(readOnly = true)
  List<ReviewEntity> findByProductId(int productId);
}
