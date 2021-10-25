package com.packtpub.microservices.springboot.utils.http;

import com.packtpub.microservices.springboot.apis.exceptions.CriticalInvocationException;
import com.packtpub.microservices.springboot.apis.exceptions.InvalidInputException;
import com.packtpub.microservices.springboot.apis.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author dougdb
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, NotFoundException ex) {
        log.info("NOT_FOUND Exception advice invoked");
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    /**
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody
    HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, InvalidInputException ex) {
        log.info("UNPROCESSABLE_ENTITY Exception advice invoked");
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    /**
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CriticalInvocationException.class)
    public @ResponseBody
    HttpErrorInfo handleCriticalInvocationError(ServerHttpRequest request, CriticalInvocationException ex) {
        log.info("INTERNAL_SERVER_ERROR Exception advice invoked");
        return createHttpErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
    }

    /**
     * @param httpStatus
     * @param request
     * @param ex
     * @return
     */
    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
