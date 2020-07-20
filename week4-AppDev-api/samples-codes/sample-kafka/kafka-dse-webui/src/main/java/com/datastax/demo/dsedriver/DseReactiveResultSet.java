package com.datastax.demo.dsedriver;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

/**
 * ResultSet.
 *
 * @author DataStax Evangelist Team
 */
public class DseReactiveResultSet {

  /** simple resultset we are used to. */
  private final ResultSet resultSet;

  /**
   * Reactive RS.
   *
   * @param resultSet
   */
  public DseReactiveResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  /**
   * Get rows.
   *
   * @return return flux of Rows with no mapping
   */
  public Flux<Row> rows() {
    return getRows(Mono.just(this.resultSet));
  }

  /**
   * @param nextResults
   * @return
   */
  private Flux<Row> getRows(Mono<ResultSet> nextResults) {
    return nextResults.flatMapMany(
        it -> {
          Flux<Row> rows = DseReactiveResultSet.toRows(it);
          if (it.isFullyFetched()) {
            return rows;
          }
          MonoProcessor<ResultSet> processor = MonoProcessor.create();
          return rows.doOnComplete(
                  () -> DseReactiveResultSet.fetchMoreRows(it.fetchMoreResults(), processor))
              .concatWith(getRows(processor));
        });
  }

  static Flux<Row> toRows(ResultSet resultSet) {
    int prefetch = Math.max(1, resultSet.getAvailableWithoutFetching());
    return Flux.fromIterable(resultSet).take(prefetch);
  }

  public static void fetchMoreRows(
      ListenableFuture<ResultSet> future, MonoProcessor<ResultSet> sink) {
    try {
      future.addListener(
          () -> {
            try {
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
