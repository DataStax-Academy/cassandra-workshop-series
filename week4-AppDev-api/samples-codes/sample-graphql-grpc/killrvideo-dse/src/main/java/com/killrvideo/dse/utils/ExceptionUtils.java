package com.killrvideo.dse.utils;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Utilities for exceptions to publish messages.
 *
 * @author DataStax evangelist team.
 */
public class ExceptionUtils {

    /**
     * Hide constructor.
     */
    private ExceptionUtils() {}
    
    /**
     * Dump a stacktrace in a String,
     * 
     * @param throwable
     *      current exception raised by the program
     * @return
     *      merged stack trace.
     */
    public static final String mergeStackTrace(Throwable throwable) {
        StringJoiner joiner = new StringJoiner("\n\t", "\n", "\n");
        joiner.add(throwable.getMessage());
        Arrays.asList(throwable.getStackTrace()).forEach(stackTraceElement -> joiner.add(stackTraceElement.toString()));
        return joiner.toString();
    }
    
}
