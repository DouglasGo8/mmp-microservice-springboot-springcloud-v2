package com.packtpub.microservices.springboot.product.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author dougdb
 */
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String> {
  Optional<ProductEntity> findByProductId(int productId);
}
