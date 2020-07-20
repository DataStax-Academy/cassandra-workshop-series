package com.datastax.demo.springdata;

import com.datastax.demo.conf.DseConstants;
import com.datastax.demo.springdata.dto.TickData;
import com.datastax.demo.springdata.dto.TickDataPrimaryKey;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/** Retrieve all Tick from Table. */
public interface TickSpringDataRepository
    extends ReactiveCrudRepository<TickData, TickDataPrimaryKey> {

  @Query("SELECT * FROM " + DseConstants.STOCKS_TICKS + " WHERE symbol = ?0 LIMIT 100")
  Flux<TickData> findBySymbol(String symbol);

  @Query(
      "SELECT * FROM "
          + DseConstants.STOCKS_TICKS
          + " WHERE symbol in ('BAC', 'DVMT','DIS','IBM','WMT') LIMIT 500")
  Flux<TickData> findAllLastSymbols();
}
