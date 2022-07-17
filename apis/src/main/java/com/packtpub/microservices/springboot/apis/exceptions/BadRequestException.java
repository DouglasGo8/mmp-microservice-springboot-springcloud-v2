package com.packtpub.microservices.springboot.apis.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
  public BadRequestException(Throwable cause) {
    super(cause);
  }
}
