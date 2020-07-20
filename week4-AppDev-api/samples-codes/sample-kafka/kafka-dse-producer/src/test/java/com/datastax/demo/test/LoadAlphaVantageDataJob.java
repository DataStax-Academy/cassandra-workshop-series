package com.datastax.demo.test;

import com.datastax.demo.conf.DseConfiguration;
import com.datastax.demo.dao.DseDao;
import com.datastax.demo.domain.Stock;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.patriques.AlphaVantageConnector;
import org.patriques.BatchStockQuotes;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.quote.BatchStockQuotesResponse;
import org.patriques.output.quote.data.StockQuote;
import org.patriques.output.timeseries.data.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "/config-test.properties")
@ContextConfiguration(classes = {DseConfiguration.class, DseDao.class})
@Ignore
public class LoadAlphaVantageDataJob {

  @Value("${alphavantage.apiKey}")
  private String apiKey;

  @Value("${alphavantage.timeout}")
  private int apiTimeout;

  @Autowired private DseDao dseDao;

  private TimeSeries stockTimeSeries;

  @Test
  public void runStocks() {
    AlphaVantageConnector apiConnector = new AlphaVantageConnector("2HWDTH7BA7FRBP76", apiTimeout);
    BatchStockQuotes bsq = new BatchStockQuotes(apiConnector);
    BatchStockQuotesResponse res = bsq.quote(dseDao.getSymbolsNYSE().toArray(new String[] {}));
    for (StockQuote sq : res.getStockQuotes()) {
      System.out.println(sq.getSymbol() + "-" + sq.getTimestamp() + "-" + sq.getPrice());
    }
  }

  @Test
  @DisplayName("Test CSV Parson")
  public void load1MinData() throws Exception {
    AlphaVantageConnector apiConnector = new AlphaVantageConnector("2HWDTH7BA7FRBP76", apiTimeout);
    stockTimeSeries = new TimeSeries(apiConnector);
    for (String symbol : dseDao.getSymbolsNYSE()) {
      System.out.println("Grabbing ... " + symbol);
      Thread.sleep(1000);
      try {
        stockTimeSeries
            .intraDay(symbol, Interval.ONE_MIN, OutputSize.FULL)
            .getStockData()
            .stream()
            .map(item -> mapToTick(symbol, item))
            .forEach(dseDao::saveStock1Min);

        /*stockTimeSeries.intraDay(symbol, Interval.SIXTY_MIN, OutputSize.FULL)
        .getStockData().stream()
        .map(item -> mapToTick(symbol, item))
        .forEach(dseDao::saveStock1Hour);*/
      } catch (RuntimeException error) {
        System.out.println("Error FOR " + symbol + " " + error.getMessage());
      }
    }
  }

  private Stock mapToTick(String symbol, StockData item) {
    Stock tick = new Stock();
    tick.setSymbol(symbol);
    tick.setClose(item.getClose());
    tick.setOpen(item.getOpen());
    tick.setLow(item.getLow());
    tick.setHigh(item.getHigh());
    tick.setVolume(item.getVolume());
    tick.setValueDate(Date.from(item.getDateTime().atZone(ZoneId.systemDefault()).toInstant()));
    return tick;
  }
}
