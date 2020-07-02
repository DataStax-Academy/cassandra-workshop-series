package com.datastax.astra.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex) {
        LOGGER.error("Illegal Argument : {}", ex.getMessage());
        return ex.getMessage();
    }
    
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String handleUnAuthorized(IllegalStateException ex) {
        LOGGER.error("Illegal State : {}", ex.getMessage());
        return ex.getMessage();
    }
    
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDefaultError(RuntimeException ex) {
        LOGGER.error("Default Error : {}", ex.getMessage());
        return ex.getMessage();
    }
    
}

