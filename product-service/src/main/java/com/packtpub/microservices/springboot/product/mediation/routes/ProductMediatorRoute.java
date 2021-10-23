package com.packtpub.microservices.springboot.product.mediation.routes;


import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@NoArgsConstructor
public class ProductMediatorRoute extends RouteBuilder {

  @Override
  public void configure() {
    from("{{direct.product.mediator.endpoint}}")
            .to("bean:productBean")
            .end();
  }
}
