package com.packtpub.microservices.springboot.product.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author dougdb
 */
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, String>
        /*PagingAndSortingRepository<ProductEntity, String>*/ {
  //Optional<ProductEntity> findByProductId(int productId);
 Mono<ProductEntity> findByProductId(int productId);
}
