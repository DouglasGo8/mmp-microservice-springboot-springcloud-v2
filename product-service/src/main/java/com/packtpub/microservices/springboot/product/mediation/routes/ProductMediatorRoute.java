package com.packtpub.microservices.springboot.product.mediation.routes;


import com.packtpub.microservices.springboot.product.beans.ProductBean;
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
            .transform(method(ProductBean.class, "getProductById"))
            .end();
  }
}
