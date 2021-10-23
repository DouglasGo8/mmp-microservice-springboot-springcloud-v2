package com.packtpub.microservices.springboot.review.mediation.routes;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@NoArgsConstructor
public class ReviewMediatorRoute extends RouteBuilder {

  @Override
  public void configure() {
    from("{{direct.review.mediator.endpoint}}")
            .to("bean:reviewBean")
            .end();
  }
}
