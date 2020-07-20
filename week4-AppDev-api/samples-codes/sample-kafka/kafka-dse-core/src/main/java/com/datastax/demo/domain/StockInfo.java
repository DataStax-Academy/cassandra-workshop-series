package com.datastax.demo.domain;

import static com.datastax.demo.conf.DseConstants.STOCKS_INFOS;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import java.io.Serializable;

/** Value for Ticks. */
@Table(name = STOCKS_INFOS)
public class StockInfo implements Serializable {

  /** serial. */
  private static final long serialVersionUID = 5806346188526710465L;

  /** value. */
  @PartitionKey private String exchange;

  /** Value Date. */
  @ClusteringColumn private String name;

  /** code. */
  @Column private String symbol;

  /** value. */
  @Column private String industry;

  /** Default Constructor */
  public StockInfo() {}

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

  /**
   * Getter accessor for attribute 'industry'.
   *
   * @return current value of 'industry'
   */
  public String getIndustry() {
    return industry;
  }

  /**
   * Setter accessor for attribute 'industry'.
   *
   * @param industry new value for 'industry '
   */
  public void setIndustry(String industry) {
    this.industry = industry;
  }

  /**
   * Getter accessor for attribute 'exchange'.
   *
   * @return current value of 'exchange'
   */
  public String getExchange() {
    return exchange;
  }

  /**
   * Setter accessor for attribute 'exchange'.
   *
   * @param exchange new value for 'exchange '
   */
  public void setExchange(String exchange) {
    this.exchange = exchange;
  }
}
