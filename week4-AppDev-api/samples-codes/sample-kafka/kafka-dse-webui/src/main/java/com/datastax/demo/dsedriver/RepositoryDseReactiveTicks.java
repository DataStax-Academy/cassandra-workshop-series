package com.datastax.demo.dsedriver;

import com.datastax.demo.conf.DseConstants;
import com.datastax.demo.domain.StockInfo;
import com.datastax.demo.domain.StockTick;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RepositoryDseReactiveTicks extends DseReactiveDaoSupport {

  /**
   * Select * FROM ticker_info
   *
   * <p>Full scan possible as tiny table with the metadata of the ticks.
   */
  public Flux<StockInfo> findAllTickerInfos() {
    // Use Driver normally with Async operations and everything not blocking
    ListenableFuture<Result<StockInfo>> futureDriver =
        mappingManager
            .mapper(StockInfo.class)
            .mapAsync(
                dseSession.executeAsync(
                    QueryBuilder.select()
                        .from(DseConstants.STOCKS_INFOS)
                        .where(QueryBuilder.eq("exchange", "NYSE"))));
    return asFlux(futureDriver);

    // return findAll(TickerInfo.class);
  }

  public Mono<StockInfo> findTickerInfo(String symbol) {
    return findOne(StockInfo.class, symbol);
  }

  public Flux<StockTick> findAllTickers() {
    return findAll(StockTick.class);
  }
}
