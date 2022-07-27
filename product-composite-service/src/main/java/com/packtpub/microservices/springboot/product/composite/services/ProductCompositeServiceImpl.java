package com.packtpub.microservices.springboot.product.composite.services;

import com.packtpub.microservices.springboot.apis.composite.*;
import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.product.composite.beans.product.ProductBean;
import com.packtpub.microservices.springboot.product.composite.beans.recommendation.RecommendationBean;
import com.packtpub.microservices.springboot.product.composite.beans.review.ReviewBean;
import com.packtpub.microservices.springboot.product.composite.dto.ProductAggregateDto;
import com.packtpub.microservices.springboot.utils.http.ServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dougdb
 */
@Slf4j
@RestController
@AllArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {

  private final ReviewBean reviewBean;
  private final ProductBean productBean;
  private final RecommendationBean recommendationBean;

  private final ServiceUtil serviceUtil;
  //
  // private final ProducerTemplate producerTemplate;

  @Override
  @SuppressWarnings({"unchecked"})
  public /*ProductAggregate*/ Mono<ProductAggregate> getProduct(int productId) {
    // var dto = producerTemplate.requestBody(
    //        "{{direct.product.composite.mediator.endpoint}}", productId, ProductAggregateDto.class);
    // return this.createProductAggregate(dto);

    log.info("Will call the getProduct with Id {}", productId);

    //log.info("{}", productBean.getProduct(productId).block().getProductName());

    return Mono.zip(values -> this.createProductAggregate(new ProductAggregateDto(
                    //
                    // Get result indexed by Position in Aggregate result
                    (Product) values[0],
                    (List<Review>) values[1],
                    (List<Recommendation>) values[2])
            ),
            // Parallel Async Services Core Invocations
            this.productBean.getProduct(productId),
            this.reviewBean.getReviews(productId).collectList(),
            this.recommendationBean.getRecommendations(productId).collectList());

    // return null;
  }

  @Override
  public /*void*/ Mono<Void> createProduct(ProductAggregate body) {
    log.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.getProductId());
    //this.producerTemplate.asyncSendBody("{{seda.product.create.composite.mediator.endpoint}}", body);

    var monoList = new ArrayList<Mono>();

    try {
      // product
      var product = new Product(body.getProductId(), body.getProductWeight(),
              body.getProductName(), null);

      monoList.add(productBean.createProduct(product));

    } catch (RuntimeException ex) {
      log.warn("createCompositeProduct failed: {}", ex.toString());
      throw ex;
    }
    //
    log.debug("createCompositeProduct: composite entities created for productId: {}", body.getProductId());
    //
    return Mono.zip(r -> "", monoList.toArray(new Mono[0]))
            .doOnError(ex -> log.warn("createCompositeProduct failed: {}", ex.toString())).then();
  }

  @Override
  public /*void*/ Mono<Void> deleteProduct(int productId) {
    log.debug("deleteCompositeProduct: Deletes a product aggregate for productId: {}", productId);
    // this.producerTemplate.asyncSendBody("{{seda.product.delete.composite.mediator.endpoint}}", productId);
    log.debug("deleteCompositeProduct: aggregate entities deleted for productId: {}", productId);
    return null;
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
