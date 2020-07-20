package com.datastax.demo.controller;

import com.datastax.demo.domain.StockTick;
import com.datastax.demo.springdata.TickSpringDataRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Service providing informations for UI. */
@Controller
public class TickerController {

  /** Map. */
  private Map<String, Flux<StockTick>> symbolsFlux = new HashMap<>();

  @Autowired private TickSpringDataRepository springRepo;

  @GetMapping(path = "/tickers/streams", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseBody
  public Flux<StockTick> fetchTickerSpringData() {
    return springRepo
        .findAllLastSymbols()
        .flatMap(
            tickData -> {
              StockTick t = new StockTick();
              t.setSymbol(tickData.getTickDataKey().getSymbol());
              t.setValue(tickData.getValue());
              t.setValueDate(tickData.getTickDataKey().getMyDate().getTime());
              return Mono.just(t);
            });
  }

  @GetMapping(
    path = "/tickers/stream/symbol/{symbol}",
    produces = MediaType.TEXT_EVENT_STREAM_VALUE
  )
  @ResponseBody
  public Flux<StockTick> fetchTickerSpringData(@PathVariable("symbol") String symbol) {
    if (!symbolsFlux.containsKey(symbol)) {
      Flux<StockTick> springFeed =
          springRepo
              .findBySymbol(symbol)
              .flatMap(
                  tickData -> {
                    StockTick t = new StockTick();
                    t.setSymbol(tickData.getTickDataKey().getSymbol());
                    t.setValue(tickData.getValue());
                    t.setValueDate(tickData.getTickDataKey().getMyDate().getTime());
                    return Mono.just(t);
                  });
      symbolsFlux.put(symbol, springFeed);
    }
    return symbolsFlux.get(symbol);
  }
}
