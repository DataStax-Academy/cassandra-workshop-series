package com.datastax.demo.domain;

import static com.datastax.demo.conf.DseConstants.STOCKS_TICKS;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import java.io.Serializable;

/** Value for Ticks. */
@Table(name = STOCKS_TICKS)
public class StockTick implements Serializable {

  /** serial. */
  private static final long serialVersionUID = 5806346188526710465L;

  /** code. */
  @PartitionKey private String symbol;

  /** Value Date. */
  @ClusteringColumn private long valueDate;

  /** value. */
  @Column private double value;

  /** Default Constructor */
  public StockTick() {}

  /** Constructor with parameters. */
  public StockTick(String tickSymbol, double value) {
    this(tickSymbol, value, System.currentTimeMillis());
  }

  /** Constructor with parameters. */
  public StockTick(String tickSymbol, double value, long valueDate) {
    this.symbol = tickSymbol;
    this.value = value;
    this.valueDate = valueDate;
  }

  /**
   * Getter accessor for attribute 'value'.
   *
   * @return current value of 'value'
   */
  public double getValue() {
    return value;
  }

  /**
   * Setter accessor for attribute 'value'.
   *
   * @param value new value for 'value '
   */
  public void setValue(double value) {
    this.value = value;
  }

  /**
   * Getter accessor for attribute 'valueDate'.
   *
   * @return current value of 'valueDate'
   */
  public long getValueDate() {
    return valueDate;
  }

  /**
   * Setter accessor for attribute 'valueDate'.
   *
   * @param valueDate new value for 'valueDate '
   */
  public void setValueDate(long valueDate) {
    this.valueDate = valueDate;
  }

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
}
