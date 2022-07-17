package com.packtpub.microservices.springboot.product.composite.beans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import com.packtpub.microservices.springboot.utils.http.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
public abstract class CommonOpsBean {

  private ObjectMapper mapper;

  @PostConstruct
  public void setUp() {
    this.mapper = new ObjectMapper();
  }

  protected RuntimeException handleHttpClientException(HttpClientErrorException ex) {
    switch (ex.getStatusCode()) {

      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(ex));

      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(ex));

      default:
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
  }

  protected Throwable handleHttpClientException(Throwable ex) {

    if (!(ex instanceof WebClientResponseException)) {
      log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
      return ex;
    }

    WebClientResponseException wcre = (WebClientResponseException)ex;

    switch (wcre.getStatusCode()) {

      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(wcre));

      case UNPROCESSABLE_ENTITY :
        return new InvalidInputException(getErrorMessage(wcre));

      default:
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        log.warn("Error body: {}", wcre.getResponseBodyAsString());
        return ex;
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }

  private String getErrorMessage(WebClientResponseException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }
}
