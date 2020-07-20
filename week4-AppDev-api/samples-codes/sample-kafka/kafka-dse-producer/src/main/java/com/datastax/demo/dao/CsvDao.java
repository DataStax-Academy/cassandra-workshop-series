package com.datastax.demo.dao;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import com.datastax.demo.domain.StockInfo;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.Spliterator;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class CsvDao {

  @Value("${csvStocksMetadata}")
  protected String csvFileName;

  /** Init table stocks_infos (used in home page of webUI). */
  public Stream<StockInfo> readStockInfosFromCsv() {
    try {
      return stream(
          spliteratorUnknownSize(
              new CsvMapper()
                  .readerFor(StockInfo.class)
                  .with(
                      CsvSchema.emptySchema()
                          .withHeader()
                          .withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR))
                  .readValues(new File(csvFileName)),
              Spliterator.ORDERED),
          false);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot filled table");
    }
  }
}
