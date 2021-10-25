package com.packtpub.microservices.springboot.product.composite.mediation.beans.review;


import com.packtpub.microservices.springboot.apis.core.review.Review;
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
public class ReviewServiceClientBean {

    private final String reviewServicePort;
    private final String reviewServiceHost;
    private final RestTemplate restTemplate;


    @Autowired
    public ReviewServiceClientBean(
            final RestTemplate restTemplate,
            @Value("${app.review-service.port}") String reviewServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost) {
        this.restTemplate = restTemplate;
        //
        this.reviewServicePort = reviewServicePort;
        this.reviewServiceHost = reviewServiceHost;
    }


    @Handler
    @SneakyThrows
    public ProductAggregateDto getReviews(final @Body ProductAggregateDto dto) {

        var productId = dto.getProduct().getProductId();

        final var reviewServiceUrl = String.format("http://" + this.reviewServiceHost + ":" +
                this.reviewServicePort + "/review?productId=%d", productId);

        log.info("Will call getReviews API on URL {}", reviewServiceUrl);

        var reviews = this.restTemplate.exchange(reviewServiceUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
                }).getBody();
        dto.setReviews(reviews);

        log.info("Found {} reviews for a product with id: {}", reviews.size(), productId);
        return dto;
    }
}
