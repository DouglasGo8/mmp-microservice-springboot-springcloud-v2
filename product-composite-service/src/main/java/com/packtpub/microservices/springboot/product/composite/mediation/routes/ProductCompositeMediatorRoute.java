package com.packtpub.microservices.springboot.product.composite.mediation.routes;


import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.product.composite.mediation.dto.ProductAggregateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductCompositeMediatorRoute extends RouteBuilder {


  @Override
  public void configure() {

    onException(HttpClientErrorException.class)
            .continued(false) // default false
            .process(e -> {
              final var exception = e.getProperty(Exchange.EXCEPTION_CAUGHT, HttpClientErrorException.class);
              e.getIn().setBody(exception.getStatusCode());
            })
            //.log(" ==> ${body}")
            .choice()
              .when(simple("${body} == 'NOT_FOUND'"))
                .throwException(new NotFoundException("!!OOPS - ProductId Not Found, productId is correct?!!"))
              .when(simple("${body} == 'UNPROCESSABLE_ENTITY'"))
                .throwException(new InvalidInputException("!!HEY - ProductId is not Valid?!!"))
            .otherwise()
              .log(LoggingLevel.ERROR, "Got an unexpected HTTP error: ${body}")
              .throwException(new RuntimeException(simple("${body}").getText()))
            .end();

    from("{{direct.product.composite.mediator.endpoint}}")
            .circuitBreaker()
              .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(2000).end()
              .to("bean:productServiceClientBean")
              .log("${body}")
            .end()
            .process(e -> {
              var dto = new ProductAggregateDto();
              e.getIn().setBody(dto);
            });
  }
}
