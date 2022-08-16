package com.packtpub.microservices.springboot.recommendation.routing;


import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.recommendation.Recommendation;
import com.packtpub.microservices.springboot.apis.core.recommendation.RecommendationService;
import lombok.NoArgsConstructor;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@NoArgsConstructor
public class RecommendationEventConsumerRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    /*from("{{direct.recommendation.mediator.getRecommendations.endpoint}}")
            .transform(method(RecommendationBean.class, "getRecommendations"))
            .end();

    from("{{direct.recommendation.mediator.createRecommendation.endpoint}}")
            .transform(method(RecommendationBean.class, "createRecommendation"))
            .end();

    from("{{direct.recommendation.mediator.deleteRecommendation.endpoint}}")
            .transform(method(RecommendationBean.class, "deleteRecommendation"))
            .end();*/

    from("{{kafka.topic.recommendations.endpoint}}")
            .choice()
              .when(simple("${body.getEventType()} == 'CREATE'"))
                .to(ExchangePattern.InOut,"direct:processRecommendationEvent")
                //.bean(RecommendationService.class, "createRecommendation")
              .when(simple("${body.getEventType()} == 'DELETE'"))
                .to(ExchangePattern.InOut,"direct:processRecommendationEvent")
                .transform(simple("${body.getProductId()}"))
                //.bean(ProductService.class, "deleteProduct")
            .end();
    //
    from("direct:processRecommendationEvent")
            .transform(simple("${body.getData()}"))
            .marshal().json(JsonLibrary.Jackson)
            .log("${body}")
            // insert on MongoDb
            .unmarshal().json(Recommendation.class);

  }


}
