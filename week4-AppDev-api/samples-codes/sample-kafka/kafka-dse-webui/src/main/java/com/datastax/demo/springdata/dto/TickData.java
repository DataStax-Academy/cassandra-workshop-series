package com.datastax.demo.springdata.dto;

import com.datastax.demo.conf.DseConstants;
import java.io.Serializable;
import java.util.Date;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Data into Cassandra.
 *
 * @author DataStax Evangelist Team
 */
@Table(value = DseConstants.STOCKS_TICKS)
public class TickData implements Serializable {

  /** Serial. */
  private static final long serialVersionUID = 6761984069893402714L;

  @PrimaryKey private TickDataPrimaryKey tickDataKey;

  /** value. */
  @Column private double value;

  /** Keep default. */
  public TickData() {}

  /** Common constructor */
  public TickData(Date datetime, TickDataPrimaryKey tickDataKey, double value) {
    this.tickDataKey = tickDataKey;
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "TickData [" + "symbol=" + tickDataKey + ", " + "value=" + value + "]";
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
   * Getter accessor for attribute 'tickDataKey'.
   *
   * @return current value of 'tickDataKey'
   */
  public TickDataPrimaryKey getTickDataKey() {
    return tickDataKey;
  }

  /**
   * Setter accessor for attribute 'tickDataKey'.
   *
   * @param tickDataKey new value for 'tickDataKey '
   */
  public void setTickDataKey(TickDataPrimaryKey tickDataKey) {
    this.tickDataKey = tickDataKey;
  }
}
