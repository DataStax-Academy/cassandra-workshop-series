package com.datastax.demo.springdata.dto;

import com.datastax.driver.core.DataType.Name;
import java.io.Serializable;
import java.util.Date;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TickDataPrimaryKey implements Serializable {

  /** Serial. */
  private static final long serialVersionUID = 1142109498800363080L;

  /** Tick Data Partition Key */
  @PrimaryKeyColumn(name = "symbol", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  @CassandraType(type = Name.TEXT)
  private String symbol;

  /** Tick Data Clustering Column */
  @PrimaryKeyColumn(name = "valueDate", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
  @CassandraType(type = Name.TIMESTAMP)
  private Date myDate;

  /**
   * Getter accessor for attribute 'symbol'.
   *
   * @return current value of 'symbol'
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Setter accessor for attribute 'symbol'.
   *
   * @param symbol new value for 'symbol '
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Getter accessor for attribute 'myDate'.
   *
   * @return current value of 'myDate'
   */
  public Date getMyDate() {
    return myDate;
  }

  /**
   * Setter accessor for attribute 'myDate'.
   *
   * @param myDate new value for 'myDate '
   */
  public void setMyDate(Date myDate) {
    this.myDate = myDate;
  }
}
