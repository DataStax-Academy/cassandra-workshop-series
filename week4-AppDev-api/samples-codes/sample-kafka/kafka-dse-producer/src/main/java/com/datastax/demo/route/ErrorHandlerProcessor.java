package com.datastax.demo.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Custom Behaviour to handle Error. */
@Component
public class ErrorHandlerProcessor extends RouteBuilder implements Processor {

  /** logger. */
  private static Logger logger = LoggerFactory.getLogger(ErrorHandlerProcessor.class);

  /** {@inheritDoc} */
  public void configure() throws Exception {
    errorHandler(deadLetterChannel("seda:errors"));
    from("seda:errors").bean(this);
  }

  /**
   * @param exchange current camel exchange
   * @throws Exception
   */
  public void process(Exchange exchange) throws Exception {
    Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
    if (cause != null) {
      logger.error("A technical error has occurred: ", cause);
    }
    logger.error("ExchangeiD" + exchange.getExchangeId());
    logger.error("Incoming" + exchange.getFromRouteId());
  }
}
