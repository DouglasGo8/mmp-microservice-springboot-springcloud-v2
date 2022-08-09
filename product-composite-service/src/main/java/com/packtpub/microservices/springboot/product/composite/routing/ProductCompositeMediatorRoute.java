package com.packtpub.microservices.springboot.product.composite.routing;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

/**
 * @author dougdb
 */
@Slf4j
@Component
@NoArgsConstructor
public class ProductCompositeMediatorRoute extends RouteBuilder {

  @Override
  public void configure() {

   /* onException(HttpClientErrorException.class)
            .continued(false) // ::false:: is default, here only demonstration purpose
            .process(e -> {
              final var exception = e.getProperty(Exchange.EXCEPTION_CAUGHT,
                      HttpClientErrorException.class);
              e.getIn().setBody(exception.getStatusCode());
            })
            //.log(LoggingLevel.ERROR, "****** ${body} ******")
            .choice()
              .when(simple("${body} == 'NOT_FOUND'"))
                .throwException(new NotFoundException("!!OOPS - NOT_FOUND_PRODUCT_ID!!"))
              .when(simple("${body} == 'UNPROCESSABLE_ENTITY'"))
                .throwException(new InvalidInputException("!!OOPS - INVALID_PRODUCT_ID!!"))
              .otherwise()
                .throwException(new CriticalInvocationException("::CRITICAL FAIL:: POSSIBLE_SERVICE_DOWN!!!"))
            .end();

    onException(ResourceAccessException.class, TimeoutException.class, UnknownHostException.class)
            .throwException(new CriticalInvocationException("::CRITICAL FAIL:: POSSIBLE_SERVICE_DOWN!!!"))
            .end();*/

    /*from("{{direct.product.composite.mediator.endpoint}}")
            // -------------------------------------------------------
            .circuitBreaker().inheritErrorHandler(true)
              .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
              .to("bean:productBean?method=getProduct")
              .setProperty("product", body())
            .end() //end circuit breaker PRODUCT_SERVICE
            .circuitBreaker().inheritErrorHandler(true)
              .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
              .to("bean:recommendationBean?method=getRecommendations")
              .setProperty("recommendations", body())
            .end() //end circuit breaker RECOMMENDATION_SERVICE
            .circuitBreaker().inheritErrorHandler(true)
              .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
              .to("bean:reviewBean?method=getReviews")
              .setProperty("reviews", body())
            .end() //end circuit breaker REVIEW_SERVICE
            // .log("${body}")
            .end();

    from("{{seda.product.create.composite.mediator.endpoint}}")
            .log("Creating Product ${body} - ${threadName}")
            .multicast().streaming()
            .parallelProcessing().executorService(Executors.newFixedThreadPool(3))
            .to(ExchangePattern.InOnly,
                    "bean:productBean?method=createProduct",
                    "bean:recommendationBean?method=createRecommendation",
                    "bean:reviewBean?method=createReview")
            .end();

    from("{{seda.product.delete.composite.mediator.endpoint}}")
            .log("Deleting Product${body} - ${threadName}")
            .multicast().streaming()
            .parallelProcessing().executorService(Executors.newFixedThreadPool(3))
            .to(ExchangePattern.InOnly,
                    "bean:productBean?method=deleteProduct",
                    "bean:recommendationBean?method=deleteRecommendation",
                    "bean:reviewBean?method=deleteReview")
            .end();
     */

    from("{{seda.event.kafka.create.product}}")
            .log(LoggingLevel.DEBUG, "Sending a ${body.getEventType()} message to Kafka")
            .setHeader(KafkaConstants.KEY, simple("${body.getKey()}"))
            .to("{{kafka.topic.products.endpoint}}");

    from("{{seda.event.kafka.create.recommendation}}")
            .log(LoggingLevel.DEBUG, "Sending a ${body.getEventType()} message to Kafka")
            .setHeader(KafkaConstants.KEY, simple("${body.getKey()}"))
            .to("{{kafka.topic.recommendations.endpoint}}");

  }
}
