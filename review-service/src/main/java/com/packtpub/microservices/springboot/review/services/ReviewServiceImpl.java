package com.packtpub.microservices.springboot.review.services;

import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.apis.core.review.ReviewService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.review.mediation.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ProducerTemplate producerTemplate;

  @Override
  public List<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 213) {
      log.debug("No reviews found for productId: {}", productId);
      return List.of(new Review());
    }

    var dto = this.producerTemplate.requestBody("{{direct.review.mediator.endpoint}}", productId,
            ReviewDto.class);

    log.debug("/reviews response size: {}", dto.getReviews().size());

    return dto.getReviews();

  }
}
