package com.packtpub.microservices.springboot.product.composite.mediation.routes;


import com.packtpub.microservices.springboot.apis.exceptions.CriticalInvocationException;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.product.composite.mediation.dto.ProductAggregateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * @author dougdb
 */
@Slf4j
@Component
public class ProductCompositeMediatorRoute extends RouteBuilder {


    @Override
    @SuppressWarnings({"unchecked"})
    public void configure() {

        onException(HttpClientErrorException.class)
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
                .end();


        from("{{direct.product.composite.mediator.endpoint}}")
                // -------------------------------------------------------
                .circuitBreaker().inheritErrorHandler(true)
                    .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
                    .to("bean:productServiceClientBean")
                    .setProperty("product", body())
                .end() //end circuit breaker PRODUCT_SERVICE
                .circuitBreaker()
                    .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
                    .to("bean:recommendationServiceClientBean")
                    .setProperty("recommendations", body())
                .end() //end circuit breaker RECOMMENDATION_SERVICE
                .circuitBreaker()
                    .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(1000).end()
                    .to("bean:reviewServiceClientBean")
                    .setProperty("reviews", body())
                .end() //end circuit breaker REVIEW_SERVICE
                // .log("${body}")
        .end();
    }
}
