package com.packtpub.microservices.springboot.recommendation.services;

import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.recommendation.RecommendationService;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.recommendation.dto.RecommendationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

  private final ProducerTemplate producerTemplate;

  @Override
  public List<Recommendation> getRecommendations(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 113) {
      log.info("No recommendations found for productId: {}", productId);
      return new ArrayList<>();
    }

    var dto = this.producerTemplate
            .requestBody("{{direct.recommendation.mediator.getRecommendations.endpoint}}", productId,
                    RecommendationDto.class);

    var recommendations = dto.getRecommendations();

    log.info("/recommendation response size: {}", recommendations.size());

    return recommendations;
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    //
    var dto = this.producerTemplate
            .requestBody("{{direct.recommendation.mediator.createRecommendation.endpoint}}", body,
                    RecommendationDto.class);
    //
    return dto.getRecommendations().get(0);
  }

  @Override
  public void deleteRecommendations(int productId) {
    this.producerTemplate
            .asyncRequestBody("{{direct.recommendation.mediator.deleteRecommendation.endpoint}}",
                    productId);
  }
}
