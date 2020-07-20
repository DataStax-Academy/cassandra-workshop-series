package com.datastax.demo.dao;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import com.datastax.demo.conf.DseConstants;
import com.datastax.demo.domain.Stock;
import com.datastax.demo.domain.Stock1Hour;
import com.datastax.demo.domain.Stock1Min;
import com.datastax.demo.domain.StockInfo;
import com.datastax.demo.domain.StockTick;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder.Direction;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DseDao implements DseConstants {

  /** Internal logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DseDao.class);

  /** Hold Connectivity to DSE. */
  @Autowired DseSession dseSession;

  /** Hold Connectivity to DSE. */
  @Autowired CsvDao csvDao;

  /** Hold Driver Mapper to implement ORM with Cassandra. */
  @Autowired MappingManager mappingManager;

  /** Mapper. */
  Mapper<StockTick> stockTicksMapper;

  /** Mapper. */
  Mapper<StockInfo> stockInfoMapper;

  /** Mapper. */
  Mapper<Stock1Min> stock1minMapper;

  /** Mapper. */
  Mapper<Stock1Hour> stock1hourMapper;

  @PostConstruct
  public void createTableifNotExist() {
    // Metadata (Home page for webUI)
    dseSession.execute(
        SchemaBuilder.createTable(STOCKS_INFOS)
            .ifNotExists()
            .addPartitionKey("exchange", DataType.text())
            .addClusteringColumn("name", DataType.text())
            .addColumn("industry", DataType.text())
            .addColumn("symbol", DataType.text())
            .withOptions()
            .clusteringOrder("name", Direction.ASC)
            .buildInternal());
    LOGGER.info(
        " + Table '{}' created in keyspace '{}' (if needed)",
        STOCKS_INFOS,
        dseSession.getLoggedKeyspace());

    // Random ticks where seed is last AlphaVantage
    dseSession.execute(
        SchemaBuilder.createTable(STOCKS_TICKS)
            .ifNotExists()
            .addPartitionKey("symbol", DataType.text())
            .addClusteringColumn("valueDate", DataType.timestamp())
            .addColumn("value", DataType.cdouble())
            .withOptions()
            .clusteringOrder("valueDate", Direction.DESC)
            .buildInternal());
    LOGGER.info(
        " + Table '{}' created in keyspace '{}' (if needed)",
        STOCKS_TICKS,
        dseSession.getLoggedKeyspace());

    // Create tables for histograms
    createTableStocksIntervalIfNotExist(STOCKS_MINUTE);
    LOGGER.info(
        " + Table '{}' created in keyspace '{}' (if needed)",
        STOCKS_MINUTE,
        dseSession.getLoggedKeyspace());

    createTableStocksIntervalIfNotExist(STOCKS_HOUR);
    LOGGER.info(
        " + Table '{}' created in keyspace '{}' (if needed)",
        STOCKS_HOUR,
        dseSession.getLoggedKeyspace());

    // Init Mappers
    stockTicksMapper = mappingManager.mapper(StockTick.class);
    stockInfoMapper = mappingManager.mapper(StockInfo.class);

    // Load CSV and fill table 'stocks_infos'
    csvDao.readStockInfosFromCsv().forEach(this::saveStockInfo);
    LOGGER.info(" + Table '{}' filled with symbols found in CSV.", STOCKS_INFOS);
    LOGGER.info(
        "Connection successfully established to DSE and schema has been created.", STOCKS_INFOS);
  }

  /** Creation of tables stocks_by* */
  private void createTableStocksIntervalIfNotExist(String tableName) {
    dseSession.execute(
        SchemaBuilder.createTable(tableName)
            .ifNotExists()
            .addPartitionKey("symbol", DataType.text())
            .addClusteringColumn("value_date", DataType.timestamp())
            .addColumn("open", DataType.cdouble())
            .addColumn("close", DataType.cdouble())
            .addColumn("high", DataType.cdouble())
            .addColumn("low", DataType.cdouble())
            .addColumn("volume", DataType.bigint())
            .withOptions()
            .clusteringOrder("value_date", Direction.DESC)
            .buildInternal());
  }

  public void saveTicker(StockTick tick) {
    dseSession.executeAsync(stockTicksMapper.saveQuery(tick));
  }

  public void saveStock1Min(Stock quote) {
    dseSession.executeAsync(stock1minMapper.saveQuery(new Stock1Min(quote)));
  }

  public void saveStock1Hour(Stock quote) {
    dseSession.executeAsync(stock1hourMapper.saveQuery(new Stock1Hour(quote)));
  }

  public void saveStock1Day(Stock quote) {
    dseSession.executeAsync(stock1hourMapper.saveQuery(new Stock1Hour(quote)));
  }

  public void saveStockInfo(StockInfo ti) {
    dseSession.executeAsync(stockInfoMapper.saveQuery(ti));
  }

  public Set<String> getSymbolsNYSE() {
    return dseSession
        .execute(select("symbol").from(STOCKS_INFOS).where(eq("exchange", "NYSE")))
        .all()
        .stream()
        .map(row -> row.getString("symbol"))
        .collect(Collectors.toSet());
  }
}
