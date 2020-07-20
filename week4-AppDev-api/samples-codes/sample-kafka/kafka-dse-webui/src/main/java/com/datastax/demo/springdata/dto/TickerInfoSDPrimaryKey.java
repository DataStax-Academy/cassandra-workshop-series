package com.datastax.demo.springdata.dto;

import com.datastax.driver.core.DataType.Name;
import java.io.Serializable;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TickerInfoSDPrimaryKey implements Serializable {

  /** Serial. */
  private static final long serialVersionUID = 1142109498800363080L;

  /** Tick Data Partition Key */
  @PrimaryKeyColumn(name = "symbol", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  @CassandraType(type = Name.TEXT)
  private String symbol;

  /** Tick Data Clustering Column */
  @PrimaryKeyColumn(name = "name", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
  @CassandraType(type = Name.TEXT)
  private String name;

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
   * Getter accessor for attribute 'name'.
   *
   * @return current value of 'name'
   */
  public String getName() {
    return name;
  }

  /**
   * Setter accessor for attribute 'name'.
   *
   * @param name new value for 'name '
   */
  public void setName(String name) {
    this.name = name;
  }
}
