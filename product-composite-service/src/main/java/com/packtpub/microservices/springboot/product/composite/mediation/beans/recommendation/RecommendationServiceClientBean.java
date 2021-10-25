package com.packtpub.microservices.springboot.product.composite.mediation.beans.recommendation;


import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.product.composite.mediation.dto.ProductAggregateDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class RecommendationServiceClientBean {

    private final String recommendationServicePort;
    private final String recommendationServiceHost;
    private final RestTemplate restTemplate;


    @Autowired
    public RecommendationServiceClientBean(
            final RestTemplate restTemplate,
            @Value("${app.recommendation-service.port}") String recommendationServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost) {
        this.restTemplate = restTemplate;
        //
        this.recommendationServicePort = recommendationServicePort;
        this.recommendationServiceHost = recommendationServiceHost;
    }

    @Handler
    @SneakyThrows
    public ProductAggregateDto getRecommendations(final @Body ProductAggregateDto dto) {

        var productId = dto.getProduct().getProductId();

        final var recommendationServiceUrl = String.format("http://" + this.recommendationServiceHost + ":" +
                this.recommendationServicePort + "/recommendation?productId=%d", productId);

        log.info("Will call getRecommendations API on URL: {}", recommendationServiceUrl);

        var recommendations = this.restTemplate.exchange(recommendationServiceUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
                }).getBody();
        dto.setRecommendations(recommendations);

        log.info("Found {} reviews for a product with id: {}", recommendations.size(), productId);
        return dto;
    }
}
