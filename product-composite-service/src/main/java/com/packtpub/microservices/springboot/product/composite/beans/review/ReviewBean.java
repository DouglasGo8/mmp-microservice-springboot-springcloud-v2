package com.packtpub.microservices.springboot.product.composite.beans.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.apis.event.Event;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
  private final ProducerTemplate template;


  @Autowired
  public ReviewBean(
          /*final RestTemplate restTemplate,*/
          ObjectMapper mapper,
          ProducerTemplate template,
          WebClient.Builder webClient,
          @Value("${app.review-service.port}") String reviewServicePort,
          @Value("${app.review-service.host}") String reviewServiceHost) {
    // this.restTemplate = restTemplate;
    //
    super(mapper);
    this.template = template;
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

  public /*void*/ Mono<Review> createReview(final /*@Body ProductAggregate*/ Review body) {
    try {
      //final var reviewService = this.reviewServiceUrl("/review");
      //log.info("Will post a new review to URL: {}", reviewService);
      log.info("Review to send {}", body);
      // var review = restTemplate.postForObject(reviewService, body, Review.class);
      // assert review != null;
      // log.info("Created a review with id: {}", review.getProductId());

      var event = new Event<>(Event.Type.CREATE, body.getProductId(), body);
      this.template.asyncSendBody("{{seda.event.kafka.review}}", event);
      //
      return Mono.just(body).subscribeOn(Schedulers.single());

    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }

  public /*void*/ Mono<Void> deleteReview(final @Body int productId) {
    //final var reviewService = this.reviewServiceUrl("/review?productId=" + productId);
    //log.debug("Will call the deleteReviews API on URL: {}", reviewService);
    //restTemplate.delete(reviewService);
    log.info("Review with id {} will be deleted", productId);
    var event = new Event<>(Event.Type.DELETE, productId, null);
    this.template.asyncSendBody("{{seda.event.kafka.review}}", event);
    //
    return Mono.just(productId).subscribeOn(Schedulers.single()).then();
  }

  private String reviewServiceUrl(String dynamicUri) {
    return "http://" + this.reviewServiceHost + ":" + this.reviewServicePort + dynamicUri;
  }
}
