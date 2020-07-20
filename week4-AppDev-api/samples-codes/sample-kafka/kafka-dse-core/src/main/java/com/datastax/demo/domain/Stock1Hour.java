package com.datastax.demo.domain;

import com.datastax.demo.conf.DseConstants;
import com.datastax.driver.mapping.annotations.Table;

/** Bean to save into table for minutes aggregation. */
@Table(name = DseConstants.STOCKS_HOUR)
public class Stock1Hour extends Stock {

  /** Specialization for a dedicated table. */
  private static final long serialVersionUID = 6789940996895471880L;

  /** Specialization. */
  public Stock1Hour(Stock parent) {
    super(parent);
  }
}
