package com.packtpub.microservices.springboot.product.composite.services;

import com.packtpub.microservices.springboot.apis.composite.*;
import com.packtpub.microservices.springboot.product.composite.dto.ProductAggregateDto;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {


  private final ServiceUtil serviceUtil;
  private final ProducerTemplate producerTemplate;


  @Override
  public ProductAggregate getProduct(int productId) {
    var dto = producerTemplate.requestBody(
            "{{direct.product.composite.mediator.endpoint}}", productId, ProductAggregateDto.class);
    return this.createProductAggregate(dto);
  }

  @Override
  public void createProduct(ProductAggregate body) {
    log.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.getProductId());
    this.producerTemplate.asyncSendBody("{{seda.product.create.composite.mediator.endpoint}}", body);
  }

  @Override
  public void deleteProduct(int productId) {

  }


  private ProductAggregate createProductAggregate(final ProductAggregateDto productAggregateDto) {

    //
    final var product = productAggregateDto.getProduct();
    final var reviews = productAggregateDto.getReviews();
    final var recommendations = productAggregateDto.getRecommendations();


    // 1. Setup product info
    final var productId = product.getProductId();
    final var productName = productAggregateDto.getProduct().getProductName();
    final var productWeight = productAggregateDto.getProduct().getProductWeight();
    final var serviceAddress = serviceUtil.getServiceAddress();

    // 2. Copy summary recommendation info, if available

    final var recommendationSummaries = (recommendations == null) ? null :
            recommendations.stream()
                    .map(r -> new RecommendationSummary(r.getRate(), r.getRecommendationId(), r.getAuthor(), r.getContent()))
                    .collect(Collectors.toList());

    // 3. Copy summary review info, if available
    final var reviewSummaries = (reviews == null) ? null :
            reviews.stream().map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
                    .collect(Collectors.toList());

    // 4. Create info regarding the involved microservices addresses
    final var productAddress = productAggregateDto.getProduct().getServiceAddress();
    final var reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
    final var recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
    final var serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);
    //
    return new ProductAggregate(productId, productWeight, productName, recommendationSummaries,
            reviewSummaries, serviceAddresses);

  }
}
