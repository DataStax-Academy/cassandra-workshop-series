package com.killrvideo.rest.error;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"com.killrvideo.rest.resource"})
@Order(0)
public class RestExceptionHandler {
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "bad request")
    public void badRequestHandler() {
        // Not necessary to handle this exception
    }
    
    

}
