package com.packtpub.microservices.springboot.product.routing;


import com.packtpub.microservices.springboot.apis.core.product.Product;
import com.packtpub.microservices.springboot.apis.core.product.ProductService;
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

    from("{{kafka.topic.products.endpoint}}")
            .choice()
            .when(simple("${body.getEventType()} == 'CREATE'"))
              .to(ExchangePattern.InOut,"direct:processProductEvent")
              .bean(ProductService.class, "createProduct")
            .when(simple("${body.getEventType()} == 'DELETE'"))
              .to(ExchangePattern.InOut,"direct:processProductEvent")
              .transform(simple("${body.getProductId()}"))
              .bean(ProductService.class, "deleteProduct")
            .end();
    //
    from("direct:processProductEvent")
            .transform(simple("${body.getData()}"))
            .marshal().json(JsonLibrary.Jackson)
            .log("${body}")
            // insert on MongoDb
            .unmarshal().json(Product.class);

  }
}
