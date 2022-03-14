package com.packtpub.microservices.springboot.recommendation.routing;


import com.packtpub.microservices.springboot.recommendation.beans.RecommendationBean;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@AllArgsConstructor
public class RecommendationMediatorRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    from("{{direct.recommendation.mediator.getRecommendations.endpoint}}")
            .transform(method(RecommendationBean.class, "getRecommendations"))
            .end();

    from("{{direct.recommendation.mediator.createRecommendation.endpoint}}")
            .transform(method(RecommendationBean.class, "createRecommendation"))
            .end();

    from("{{direct.recommendation.mediator.deleteRecommendation.endpoint}}")
            .transform(method(RecommendationBean.class, "deleteRecommendation"))
            .end();

  }


}
