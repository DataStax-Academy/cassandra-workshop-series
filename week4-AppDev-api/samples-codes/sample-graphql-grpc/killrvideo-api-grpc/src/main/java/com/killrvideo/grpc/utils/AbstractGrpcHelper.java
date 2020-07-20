package com.killrvideo.grpc.utils;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * Mutualization in validator code.
 *
 * @author DataStax evangelist team.
 */
public abstract class AbstractGrpcHelper {
   
    /**
     * Enable default constructor.
     */
    public AbstractGrpcHelper() {}
    
    /**
     * Init error builder.
     *  
     * @param request
     *      current request
     * @return
     *      current error message
     */
    protected StringBuilder initErrorString(Object request) {
        return new StringBuilder("Validation error for '" + request.toString() + "' : \n");
    }
    
   /**
    * Deduplicate condition evaluation.
    *
    * @param assertion
    *      current condition
    * @param fieldName
    *      fieldName to evaluate
    * @param request
    *      GRPC reauest
    * @param errorMessage
    *      concatenation of error messages
    * @return
    */
    protected boolean notEmpty(boolean assertion, String fieldName, String request, StringBuilder errorMessage) {
       if (assertion) {
           errorMessage.append("\t\t");
           errorMessage.append(fieldName);
           errorMessage.append("should be provided for comment on ");
           errorMessage.append(request);
           errorMessage.append("\n");
       }
       return !assertion;
   }
   
    /**
     * Add error message if assertion is violated.
     * 
     * @param assertion
     *      current assertion
     * @param fieldName
     *      current field name
     * @param request
     *      current request
     * @param errorMessage
     *      current error message
     * @return
     *      if the correction is OK.
     */
    protected boolean positive(boolean assertion, String fieldName, String request, StringBuilder errorMessage) {
        if (assertion) {
            errorMessage.append("\t\t");
            errorMessage.append(fieldName);
            errorMessage.append("should be strictly positive for ");
            errorMessage.append(request);
            errorMessage.append("\n");
        }
        return !assertion;
    }

    /**
     * Utility to validate Grpc Input.
     *
     * @param streamObserver
     *      grpc observer
     * @param errorMessage
     *      error mressage
     * @param isValid
     *      validation of that
     * @return
     *      ok
     */
    protected boolean validate(Logger logger, StreamObserver<?> streamObserver, StringBuilder errorMessage, boolean isValid) {
        if (isValid) {
            return true;
        } else {
            final String description = errorMessage.toString();
            logger.error(description);
            streamObserver.onError(Status.INVALID_ARGUMENT.withDescription(description).asRuntimeException());
            streamObserver.onCompleted();
            return false;
        }
    }
    
    /**
     * Utility to TRACE.
     *
     * @param method
     *      current operation
     * @param start
     *      timestamp for starting
     */
    public void traceSuccess(Logger logger, String method, Instant starts) {
        if (logger.isDebugEnabled()) {
            logger.debug("End successfully '{}' in {} millis", method, Duration.between(starts, Instant.now()).getNano()/1000);
        }
    }
    
    /**
     * Utility to TRACE.
     *
     * @param method
     *      current operation
     * @param start
     *      timestamp for starting
     */
    public void traceError(Logger logger, String method, Instant starts, Throwable t) {
        logger.error("An error occured in {} after {}", method, Duration.between(starts, Instant.now()), t);
    }
}
