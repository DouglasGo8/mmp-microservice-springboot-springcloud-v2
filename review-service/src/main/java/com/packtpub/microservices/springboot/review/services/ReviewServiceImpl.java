package com.packtpub.microservices.springboot.review.services;

import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.apis.core.review.ReviewService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.review.beans.ReviewBean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  //private final ProducerTemplate producerTemplate;
  private final ReviewBean review;

  @Override
  public /*Review*/ Mono<Review> createReview(Review body) {
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    //return this.producerTemplate
    //        .requestBody("{{direct.createReview.mediator.endpoint}}", body, Review.class);
    this.review.createReview(body).block();
    //
    return Mono.empty();
  }

  @Override
  public /*List<Review>*/ Flux<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 213) {
      log.debug("No reviews found for productId: {}", productId);
      return Flux.empty();
    }

    /*var dto = this.producerTemplate.requestBody("{{direct.getReview.mediator.endpoint}}", productId,
            ReviewDto.class);

    log.debug("/reviews response size: {}", dto.getReviews().size());

    return dto.getReviews();*/

    return this.review.retrieveReviews(productId);

  }

  @Override
  public /*void*/ Mono<Void> deleteReviews(int productId) {
    //this.producerTemplate.asyncRequestBody("{{direct.deleteReview.mediator.endpoint}}", productId);
    return this.review.deleteReview(productId);
  }
}
