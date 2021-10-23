package com.packtpub.microservices.springboot.recommendation.mediation.routes;


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

    from("{{direct.recommendation.mediator.endpoint}}")
            .to("bean:recommendationBean")
            .end();

  }


}
