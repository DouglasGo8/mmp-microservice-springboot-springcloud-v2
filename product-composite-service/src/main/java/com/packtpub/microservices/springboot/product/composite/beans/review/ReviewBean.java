package com.packtpub.microservices.springboot.product.composite.beans.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.logging.Level;
import static reactor.core.publisher.Flux.empty;
/**
 * @author dougdb
 */
@Slf4j
@Component
public class ReviewBean extends CommonOpsBean {

  private final WebClient webClient;
  private final String reviewServicePort;
  private final String reviewServiceHost;
  //private final RestTemplate restTemplate;


  @Autowired
  public ReviewBean(
          /*final RestTemplate restTemplate,*/
          ObjectMapper mapper,
          WebClient.Builder webClient,
          @Value("${app.review-service.port}") String reviewServicePort,
          @Value("${app.review-service.host}") String reviewServiceHost) {
    // this.restTemplate = restTemplate;
    //
    super(mapper);
    this.webClient = webClient.build();
    this.reviewServicePort = reviewServicePort;
    this.reviewServiceHost = reviewServiceHost;
  }

  public /*ProductAggregateDto*/ Flux<Review> getReviews(final /*@Body ProductAggregateDto dto*/ int productId) {

    // var productId = dto.getProduct().getProductId();

    final var partialUrl = String.format("/review?productId=%d", productId);
    final var reviewServiceUri = this.reviewServiceUrl(partialUrl);
    //
    log.info("Will call getReviews API on URL {}", reviewServiceUri);
    //
    // var reviews = this.restTemplate.exchange(reviewService,
    //        HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
    //        }).getBody();
    // dto.setReviews(reviews);

    // log.info("Found {} reviews for a product with id: {}", reviews.size(), productId);

    //return dto;

    return this.webClient.get().uri(reviewServiceUri)
            .retrieve().bodyToFlux(Review.class)
            .log(log.getName(), Level.FINE)
            .onErrorResume(err -> empty());
  }

  public void createReview(final @Body ProductAggregate body) {
    try {
      final var reviewService = this.reviewServiceUrl("/review");
      log.info("Will post a new review to URL: {}", reviewService);
      // var review = restTemplate.postForObject(reviewService, body, Review.class);
      // assert review != null;
      // log.info("Created a review with id: {}", review.getProductId());
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }

  public void deleteReview(final @Body int productId) {
    final var reviewService = this.reviewServiceUrl("/review?productId=" + productId);
    log.debug("Will call the deleteReviews API on URL: {}", reviewService);
    //restTemplate.delete(reviewService);
  }

  private String reviewServiceUrl(String dynamicUri) {
    return "http://" + this.reviewServiceHost + ":" + this.reviewServicePort + dynamicUri;

  }
}
