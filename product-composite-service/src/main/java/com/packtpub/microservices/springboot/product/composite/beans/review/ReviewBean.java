package com.packtpub.microservices.springboot.product.composite.beans.review;


import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import com.packtpub.microservices.springboot.product.composite.dto.ProductAggregateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ReviewBean extends CommonOpsBean {

  private final String reviewServicePort;
  private final String reviewServiceHost;
  private final RestTemplate restTemplate;


  @Autowired
  public ReviewBean(
          final RestTemplate restTemplate,
          @Value("${app.review-service.port}") String reviewServicePort,
          @Value("${app.review-service.host}") String reviewServiceHost) {
    this.restTemplate = restTemplate;
    //
    this.reviewServicePort = reviewServicePort;
    this.reviewServiceHost = reviewServiceHost;
  }

  public ProductAggregateDto getReviews(final @Body ProductAggregateDto dto) {

    var productId = dto.getProduct().getProductId();

    final var url = String.format("/review?productId=%d", productId);
    final var reviewService = this.reviewServiceUrl(url);
    //
    log.info("Will call getReviews API on URL {}", reviewService);
    //
    var reviews = this.restTemplate.exchange(reviewService,
            HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
            }).getBody();
    dto.setReviews(reviews);

    log.info("Found {} reviews for a product with id: {}", reviews.size(), productId);
    return dto;
  }

  public void createReview(final @Body ProductAggregate body) {
    try {
      final var reviewService = this.reviewServiceUrl("/review");
      log.info("Will post a new review to URL: {}", reviewService);
      //Review review = restTemplate.postForObject(url, body, Review.class);
      //log.info("Created a review with id: {}", review.getProductId());
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }


  private String reviewServiceUrl(String dynamicUri) {
    return "http://" + this.reviewServiceHost + ":" +
            this.reviewServicePort + dynamicUri;

  }
}
