package com.datastax.demo.dsedriver;

import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

/** Work with Flux<Rows> and Mono<Row>. */
public class DseReactiveResult<M> {

  /** Embedded result. */
  private final Result<M> results;

  /**
   * Default constructor.
   *
   * @param results
   */
  public DseReactiveResult(Result<M> results) {
    this.results = results;
  }

  /**
   * Create the flux with associated processor on next.
   *
   * @return la liste des records
   */
  public Flux<M> records() {
    return getRecords(Mono.just(this.results));
  }

  /**
   * Convert from Result to FLOW.
   *
   * @param nextResults list of elements
   * @return
   */
  private Flux<M> getRecords(Mono<Result<M>> nextResults) {
    return nextResults.flatMapMany(
        it -> {
          Flux<M> rows = DseReactiveResult.toRecords(it);
          if (it.isFullyFetched()) {
            return rows;
          }
          MonoProcessor<Result<M>> processor = MonoProcessor.create();
          return rows.doOnComplete(
                  () -> DseReactiveResult.fetchMoreRecords(it.fetchMoreResults(), processor))
              .concatWith(getRecords(processor));
        });
  }

  static <T> Flux<T> toRecords(Result<T> result) {
    int prefetch = Math.max(1, result.getAvailableWithoutFetching());
    return Flux.fromIterable(result).take(prefetch);
  }

  public static <T> void fetchMoreRecords(
      ListenableFuture<Result<T>> future, MonoProcessor<Result<T>> sink) {
    try {
      future.addListener(
          () -> {
            try {
              System.out.println("FETCH");
              sink.onNext(future.get());
              sink.onComplete();
            } catch (ExecutionException cause) {
              sink.onError(cause.getCause());
            } catch (Exception cause) {
              sink.onError(cause);
            }
          },
          Runnable::run);
    } catch (Exception cause) {
      sink.onError(cause);
    }
  }
}
