package com.datastax.demo.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/** POJO Representing stock from Alpha Vantage. */
public class Stock implements Serializable {

  /** Serial. */
  private static final long serialVersionUID = -5240591446495279713L;

  /** Stock symbol. */
  @PartitionKey private String symbol;

  /** timestamp. */
  @ClusteringColumn
  @Column(name = "value_date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private Date valueDate;

  /** value at begining of period. */
  @Column private double open;

  /** value at end of period. */
  @Column private double close;

  /** low value. */
  @Column private double low;

  /** high value. */
  @Column private double high;

  /** number exchanged. */
  @Column private long volume;

  /** Default constructor (unmarshalling) */
  public Stock() {}

  /** Copy constructor (specialization) */
  public Stock(Stock parent) {
    this.valueDate = parent.getValueDate();
    this.high = parent.getHigh();
    this.low = parent.getLow();
    this.open = parent.getOpen();
    this.close = parent.getClose();
    this.volume = parent.getVolume();
    this.symbol = parent.getSymbol();
  }

  /**
   * Getter accessor for attribute 'valueDate'.
   *
   * @return current value of 'valueDate'
   */
  public Date getValueDate() {
    return valueDate;
  }

  /**
   * Setter accessor for attribute 'valueDate'.
   *
   * @param valueDate new value for 'valueDate '
   */
  public void setValueDate(Date valueDate) {
    this.valueDate = valueDate;
  }

  /**
   * Getter accessor for attribute 'open'.
   *
   * @return current value of 'open'
   */
  public double getOpen() {
    return open;
  }

  /**
   * Setter accessor for attribute 'open'.
   *
   * @param open new value for 'open '
   */
  public void setOpen(double open) {
    this.open = open;
  }

  /**
   * Getter accessor for attribute 'close'.
   *
   * @return current value of 'close'
   */
  public double getClose() {
    return close;
  }

  /**
   * Setter accessor for attribute 'close'.
   *
   * @param close new value for 'close '
   */
  public void setClose(double close) {
    this.close = close;
  }

  /**
   * Getter accessor for attribute 'low'.
   *
   * @return current value of 'low'
   */
  public double getLow() {
    return low;
  }

  /**
   * Setter accessor for attribute 'low'.
   *
   * @param low new value for 'low '
   */
  public void setLow(double low) {
    this.low = low;
  }

  /**
   * Getter accessor for attribute 'high'.
   *
   * @return current value of 'high'
   */
  public double getHigh() {
    return high;
  }

  /**
   * Setter accessor for attribute 'high'.
   *
   * @param high new value for 'high '
   */
  public void setHigh(double high) {
    this.high = high;
  }

  /**
   * Getter accessor for attribute 'volume'.
   *
   * @return current value of 'volume'
   */
  public long getVolume() {
    return volume;
  }

  /**
   * Setter accessor for attribute 'volume'.
   *
   * @param volume new value for 'volume '
   */
  public void setVolume(long volume) {
    this.volume = volume;
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
