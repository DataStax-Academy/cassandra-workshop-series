package com.datastax.demo.springdata.dto;

import com.datastax.demo.conf.DseConstants;
import java.io.Serializable;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Data into Cassandra.
 *
 * @author DataStax Evangelist Team
 */
@Table(value = DseConstants.STOCKS_INFOS)
public class TickerInfoSD implements Serializable {

  /** Serial. */
  private static final long serialVersionUID = 6761984069893402714L;

  @PrimaryKey private TickerInfoSDPrimaryKey tickerInfoDataKey;

  /** value. */
  @Column private String industry;

  /** value. */
  @Column private String exchange;

  /** Default Constructor */
  public TickerInfoSD() {}

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

  /**
   * Getter accessor for attribute 'tickerInfoDataKey'.
   *
   * @return current value of 'tickerInfoDataKey'
   */
  public TickerInfoSDPrimaryKey getTickerInfoDataKey() {
    return tickerInfoDataKey;
  }

  /**
   * Setter accessor for attribute 'tickerInfoDataKey'.
   *
   * @param tickerInfoDataKey new value for 'tickerInfoDataKey '
   */
  public void setTickerInfoDataKey(TickerInfoSDPrimaryKey tickerInfoDataKey) {
    this.tickerInfoDataKey = tickerInfoDataKey;
  }
}
