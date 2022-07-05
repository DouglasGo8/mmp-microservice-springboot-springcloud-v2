package com.packtpub.microservices.springboot.product.routing;


import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Component
@NoArgsConstructor
public class ProductEventConsumerRoute extends RouteBuilder {

  @Override
  public void configure() {

    // CamelReactiveStreamsService camel = CamelReactiveStreams.get(super.getContext());

    /*onException(DuplicateKeyException.class)
            .continued(false)
            .throwException(new InvalidInputException(body().toString()));

    from("{{direct.product.routing.createProduct.endpoint}}").routeId("createProductRoute")
            .transform(method(ProductBean.class, "createProduct"))
            .end();

    from("{{direct.product.routing.deleteProduct.endpoint}}").routeId("deleteProductRoute")
            .transform(method(ProductBean.class, "deleteProduct"))
            .end();*/

    //from("{{direct.product.routing.getProductById.endpoint}}").routeId("getProductByIdRoute")
    //        .transform(method(ProductBean.class, "getProductById"))
    //        .to("direct:bla")
    //        .end();



  }
}
