package com.packtpub.microservices.springboot.apis.core.review;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author dougdb
 */
public interface ReviewService {

  /**
   * Sample usage, see below.
   * <p>
   * curl -X POST $HOST:$PORT/review \
   * -H "Content-Type: application/json" --data \
   * '{"productId":123,"reviewId":456,"author":"me","subject":"yada, yada, yada","content":"yada, yada, yada"}'
   *
   * @param body A JSON representation of the new review
   * @return A JSON representation of the newly created review
   */
  //@PostMapping(value = "/review", consumes = "application/json", produces = "application/json")
  //Review createReview(@RequestBody Review body);
  Mono<Review> createReview(Review body);


  /**
   * @param productId Id of the product
   * @return the reviews of the product
   */
  @GetMapping(value = "/review", produces = "application/json")
  //List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);
  Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/review?productId=1".
   *
   * @param productId Id of the product
   */
  //@DeleteMapping(value = "/review")
  //void deleteReviews(@RequestParam(value = "productId", required = true) int productId);
  Mono<Void> deleteReviews(int productId);
}
