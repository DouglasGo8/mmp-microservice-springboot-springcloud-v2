package com.packtpub.microservices.springboot.product.composite.beans.recommendation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
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
public class RecommendationBean extends CommonOpsBean {

  private final String recommendationServicePort;
  private final String recommendationServiceHost;
  private final RestTemplate restTemplate;


  @Autowired
  public RecommendationBean(
          final RestTemplate restTemplate,
          @Value("${app.recommendation-service.port}") String recommendationServicePort,
          @Value("${app.recommendation-service.host}") String recommendationServiceHost) {

    this.restTemplate = restTemplate;
    //
    this.recommendationServicePort = recommendationServicePort;
    this.recommendationServiceHost = recommendationServiceHost;
  }


  public ProductAggregateDto getRecommendations(final @Body ProductAggregateDto dto) {

    var productId = dto.getProduct().getProductId();
    var url = String.format("/recommendation?productId=%d", productId);
    //
    var recommendationService = this.recommendationServiceUrl(url);

    log.info("Will call getRecommendations API on URL: {}", recommendationService);

    var recommendations = this.restTemplate.exchange(recommendationService,
            HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
            }).getBody();
    dto.setRecommendations(recommendations);

    log.info("Found {} reviews for a product with id: {}", recommendations.size(), productId);
    return dto;
  }

  public void createRecommendation(final @Body ProductAggregate body) {
    try {
      //
      var recommendationService = this.recommendationServiceUrl("/recommendation");
      log.info("Will post a new recommendation to URL: {}", recommendationService);
      if (null != body.getRecommendations()) {
        body.getRecommendations().forEach(r -> {
          var recommendation = new Recommendation(r.getRate(), body.getProductId(), r.getRecommendationId(),
                  r.getAuthor(), r.getContent(), null);
        /*var recommendationCreated = this.restTemplate.postForObject(recommendationService, recommendation,
                Recommendation.class);
        log.debug("Created a recommendation with id: {}", recommendationCreated.getProductId());*/
        });
      }
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }


  private String recommendationServiceUrl(String dynamicUri) {
    return "http://" + this.recommendationServiceHost + ":" +
            this.recommendationServicePort + dynamicUri;

  }
}
