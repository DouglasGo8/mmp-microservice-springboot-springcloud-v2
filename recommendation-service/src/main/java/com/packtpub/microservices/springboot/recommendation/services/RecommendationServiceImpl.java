package com.packtpub.microservices.springboot.recommendation.services;

import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.recommendation.RecommendationService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.recommendation.beans.RecommendationBean;
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
public class RecommendationServiceImpl implements RecommendationService {

  // private final ProducerTemplate producerTemplate;
  private final RecommendationBean recommendation;

  @Override
  public /*List<Recommendation>*/ Flux<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    //if (productId == 113) {
    //  log.info("No recommendations found for productId: {}", productId);
    //  return new ArrayList<>();
    //}
    //var dto = this.producerTemplate
    //        .requestBody("{{direct.recommendation.mediator.getRecommendations.endpoint}}", productId,
    //                RecommendationDto.class);
    //var recommendations = dto.getRecommendations();
    //log.info("/recommendation response size: {}", recommendations.size());
    log.info("Will get recommendations for product with id={}", productId);

    return this.recommendation.getRecommendations(productId);
  }

  @Override
  public /*Recommendation*/ Mono<Recommendation> createRecommendation(Recommendation body) {

    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    //
    //var dto = this.producerTemplate
    //        .requestBody("{{direct.recommendation.mediator.createRecommendation.endpoint}}", body,
    //                RecommendationDto.class);
    //
    //return dto.getRecommendations().get(0);
    return this.recommendation.createRecommendation(body);
  }

  @Override
  public /*void*/ Mono<Void> deleteRecommendations(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    //this.producerTemplate
    //        .asyncRequestBody("{{direct.recommendation.mediator.deleteRecommendation.endpoint}}",
    //                productId);

    return this.recommendation.deleteRecommendation(productId);
  }
}
