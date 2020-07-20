package com.datastax.demo.dsedriver;

import static reactor.core.publisher.Mono.fromFuture;

import com.datastax.demo.conf.DseConstants;
import com.datastax.demo.domain.StockTick;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Provides a way to convert objects returns by Java Driver to Mono<T> Flux<T> expected by Reactor
 * and Spring WebFlux.
 *
 * @author Cedrick Lunven <= for questions
 */
public abstract class DseReactiveDaoSupport implements DseConstants {

  /** Hold Connectivity to DSE. */
  @Autowired protected DseSession dseSession;

  /** Hold Driver Mapper to implement ORM with Cassandra. */
  @Autowired protected MappingManager mappingManager;

  /** Work with simple CQL and ResultSet, will need some mapper afterward or return Flux<Row> */
  protected Mono<DseReactiveResultSet> executeASync(ListenableFuture<ResultSet> future) {
    return Mono.<DseReactiveResultSet>create(
        sink -> {
          try {
            ListenableFuture<DseReactiveResultSet> resultSetFuture =
                Futures.transform(future, DseReactiveResultSet::new);
            resultSetFuture.addListener(
                () -> {
                  if (resultSetFuture.isDone()) {
                    try {
                      sink.success(resultSetFuture.get());
                    } catch (ExecutionException cause) {
                      sink.error(cause.getCause());
                    } catch (Exception cause) {
                      sink.error(cause);
                    }
                  }
                },
                Runnable::run);
          } catch (Exception cause) {
            sink.error(cause);
          }
        });
  }

  /** Working with {@link Mapper} */
  @SuppressWarnings("hiding")
  protected <T> Mono<DseReactiveResult<T>> executeAndMapASync(
      ListenableFuture<Result<T>> resultAsyncMapper) {
    return Mono.<DseReactiveResult<T>>create(
        sink -> {
          try {

            ListenableFuture<DseReactiveResult<T>> resultSetFuture =
                Futures.transform(resultAsyncMapper, DseReactiveResult<T>::new);

            resultSetFuture.addListener(
                () -> {
                  if (resultSetFuture.isDone()) {
                    try {
                      sink.success(resultSetFuture.get());
                    } catch (ExecutionException cause) {
                      sink.error(cause.getCause());
                    } catch (Exception cause) {
                      sink.error(cause);
                    }
                  }
                },
                Runnable::run);
          } catch (Exception cause) {
            sink.error(cause);
          }
        });
  }

  @SuppressWarnings("hiding")
  protected <T> Mono<T> asMono(final ListenableFuture<T> listenableFuture) {
    return fromFuture(asCompletableFuture(listenableFuture));
  }

  @SuppressWarnings("hiding")
  protected <T> Flux<T> asFlux(ListenableFuture<Result<T>> futureDriver) {
    return executeAndMapASync(futureDriver)
        .flatMapMany(DseReactiveResult::records)
        .onErrorMap(
            throwable -> new IllegalStateException("Error during Select ticker info", throwable));
  }

  @SuppressWarnings("hiding")
  protected <T> CompletableFuture<T> asCompletableFuture(
      final ListenableFuture<T> listenableFuture) {

    // create an instance of CompletableFuture
    CompletableFuture<T> completable =
        new CompletableFuture<T>() {
          @Override
          public boolean cancel(boolean mayInterruptIfRunning) {
            // propagate cancel to the listenable future
            boolean result = listenableFuture.cancel(mayInterruptIfRunning);
            super.cancel(mayInterruptIfRunning);
            return result;
          }
        };

    // add callback
    Futures.addCallback(
        listenableFuture,
        new FutureCallback<T>() {
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

  /** A little bit 'to much' probably. */
  @SuppressWarnings("hiding")
  public <T> Flux<T> findAll(Class<T> pojo) {
    String tableName = StockTick.class.getAnnotation(Table.class).name();
    Select selectQuery = QueryBuilder.select().from(tableName);
    return asFlux(mappingManager.mapper(pojo).mapAsync(dseSession.executeAsync(selectQuery)));
  }

  /** Syntaxic sugar. */
  @SuppressWarnings("hiding")
  public <T> Mono<T> findOne(Class<T> pojo, Object... args) {
    return asMono(mappingManager.mapper(pojo).getAsync(args));
  }
}
