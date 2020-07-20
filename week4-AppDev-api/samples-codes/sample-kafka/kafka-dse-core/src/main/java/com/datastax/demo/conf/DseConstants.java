package com.datastax.demo.conf;

/**
 * Constants in DSE-DB Tables.
 *
 * @author DataStax Evangelist Team
 */
public interface DseConstants {

  /** Table Names in Keyspace (Columns are defined in Beans). */
  String STOCKS_MINUTE = "stocks_by_min";

  String STOCKS_HOUR = "stocks_by_hour";
  String STOCKS_DAY = "stocks_by_day";

  String STOCKS_TICKS = "stocks_ticks";
  String STOCKS_INFOS = "stocks_infos";

  String TICKER_COL_EXCHANGE = "exchange";
  String TICKER_COL_INDUSTRY = "industry";
  String TICKER_COL_NAME = "name";
  String TICKER_COL_SYMBOL = "symbol";
}
