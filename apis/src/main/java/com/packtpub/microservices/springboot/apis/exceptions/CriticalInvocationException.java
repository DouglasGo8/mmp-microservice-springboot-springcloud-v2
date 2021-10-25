package com.packtpub.microservices.springboot.apis.exceptions;

import lombok.NoArgsConstructor;

/**
 * @author dougdb
 */
@NoArgsConstructor
public class CriticalInvocationException extends RuntimeException {
    public CriticalInvocationException(String message) {
        super(message);
    }
    public CriticalInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
    public CriticalInvocationException(Throwable cause) {
        super(cause);
    }
}
