package com.packtpub.microservices.springboot.product.composite.beans.recommendation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.composite.ProductAggregate;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.event.Event;
import com.packtpub.microservices.springboot.product.composite.beans.common.CommonOpsBean;
import com.packtpub.microservices.springboot.product.composite.dto.ProductAggregateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.logging.Level;

import static reactor.core.publisher.Flux.empty;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class RecommendationBean extends CommonOpsBean {

  private final WebClient webClient;
  private final String recommendationServicePort;
  private final String recommendationServiceHost;
  // private final RestTemplate restTemplate;
  private final ProducerTemplate template;

  @Autowired
  public RecommendationBean(
          /*final RestTemplate restTemplate,*/
          ObjectMapper mapper,
          WebClient.Builder webClient,
          ProducerTemplate template,
          @Value("${app.recommendation-service.port}") String recommendationServicePort,
          @Value("${app.recommendation-service.host}") String recommendationServiceHost) {

    //this.restTemplate = restTemplate;
    super(mapper);
    //
    this.template = template;
    this.webClient = webClient.build();
    this.recommendationServicePort = recommendationServicePort;
    this.recommendationServiceHost = recommendationServiceHost;
  }


  public /*ProductAggregateDto*/ Flux<Recommendation> getRecommendations(final /*@Body ProductAggregateDto dto*/ int productId) {

    // var productId = dto.getProduct().getProductId();
    var partialUrl = String.format("/recommendation?productId=%d", productId);
    //
    var recommendationServiceUri = this.recommendationServiceUrl(partialUrl);
    //
    log.info("Will call getRecommendations API on URL: {}", recommendationServiceUri);
    //var recommendations = this.restTemplate.exchange(recommendationService,
    //        HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
    //        }).getBody();
    //dto.setRecommendations(recommendations);
    //log.info("Found {} reviews for a product with id: {}", recommendations.size(), productId);
    // return dto;
    return this.webClient.get().uri(recommendationServiceUri)
            .retrieve().bodyToFlux(Recommendation.class)
            .log(log.getName(), Level.FINE)
            .onErrorResume(err -> empty());
  }

  public /*void*/ Mono<Recommendation> createRecommendation(final /*@Body ProductAggregate*/ Recommendation body) {
    try {
      //
      /*if (null != body.getRecommendations()) {
        var recommendationService = this.recommendationServiceUrl("/recommendation");
        log.info("Will post a new recommendation to URL: {}", recommendationService);
        //
        body.getRecommendations().forEach(r -> {
          var recommendation = new Recommendation(r.getRate(), body.getProductId(), r.getRecommendationId(),
                  r.getAuthor(), r.getContent(), null);
          // var recommendationCreated = this.restTemplate.postForObject(recommendationService, recommendation,
          //         Recommendation.class);
          //assert recommendationCreated != null;
          //log.debug("Created a recommendation with id: {}", recommendationCreated.getProductId());
        });
      }*/
      log.debug("create Recommendation: for productId: {}", body.getProductId());
      var event = new Event<>(Event.Type.CREATE, body.getProductId(), body);
      this.template.asyncSendBody("{{seda.event.kafka.recommendation}}", event);
      //
      return Mono.just(body).subscribeOn(Schedulers.single());
      //
    } catch (HttpClientErrorException ex) {
      throw super.handleHttpClientException(ex);
    }
  }

  public /*void*/ Mono<Void> deleteRecommendation(final /*@Body*/ int productId) {
    try {
      //var url = this.recommendationServiceUrl("/recommendation?productId=" + productId);
      //log.debug("Will call the deleteRecommendations API on URL: {}", url);
      //restTemplate.delete(url);
      log.info("Recommendation with id {} will be deleted", productId);
      var event = new Event<>(Event.Type.DELETE, productId, null);
      this.template.asyncSendBody("{{seda.event.kafka.recommendation}}", event);
      //
      return Mono.just(productId).subscribeOn(Schedulers.single()).then();
    } catch (HttpClientErrorException ex) {
      throw handleHttpClientException(ex);
    }
  }


  private String recommendationServiceUrl(String dynamicUri) {
    return "http://" + this.recommendationServiceHost + ":" +
            this.recommendationServicePort + dynamicUri;

  }
}
