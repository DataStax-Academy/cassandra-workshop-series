package com.killrvideo.dse.utils;

import java.util.concurrent.CompletableFuture;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Utility class to work on Futures.
 * 
 * @author DataStax evangelist team.
 */
public class FutureUtils {

    /**
     * Hide constructor.
     */
    private FutureUtils() {}
    
    /**
     * This is a utility class that converts ListenableFuture objects into CompletableFuture,
     * creates a callback, and returns CompletableFuture
     * @param listenableFuture
     * @param <T>
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> asCompletableFuture(final ListenableFuture<T> listenableFuture) {

        //create an instance of CompletableFuture
        CompletableFuture<T> completable = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                // propagate cancel to the listenable future
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        // add callback
        Futures.addCallback(listenableFuture, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                completable.complete(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completable.completeExceptionally(t);
            }
        });
        return completable;
    }
}
