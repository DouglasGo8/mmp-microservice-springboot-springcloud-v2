package com.packtpub.microservices.springboot.review.routing;

import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.review.Review;
import com.packtpub.microservices.springboot.apis.core.review.ReviewService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Slf4j
@Component
@NoArgsConstructor
public class ReviewEventConsumerRoute extends RouteBuilder {

  @Override
  public void configure() {

    /*from("{{direct.getReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "retrieveReviews"))
            .end();

    from("{{direct.createReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "createReview"))
            .end();

    from("{{direct.deleteReview.mediator.endpoint}}")
            .transform(method(ReviewBean.class, "deleteReview"))
            .end();*/

    from("{{kafka.topic.reviews.endpoint}}")
            .choice()
              .when(simple("${body.getEventType()} == 'CREATE'"))
                .to(ExchangePattern.InOut,"direct:processReviewsEvent")
                //.bean(ReviewService.class, "createReview")
              .when(simple("${body.getEventType()} == 'DELETE'"))
                .to(ExchangePattern.InOut,"direct:processReviewsEvent")
                .transform(simple("${body.getProductId()}"))
                //.bean(ProductService.class, "deleteProduct")
            .end();
    //
    from("direct:processReviewsEvent")
            .transform(simple("${body.getData()}"))
            .marshal().json(JsonLibrary.Jackson)
            .log("${body}")
            // insert on MongoDb
            .unmarshal().json(Review.class);

  }
}
