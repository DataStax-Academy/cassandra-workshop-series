package com.datastax.demo;

import com.datastax.demo.route.StockTicksConsumer;
import com.datastax.demo.route.StockTicksProducer;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Apache Camel trigger processors with routes. */
@Component
public class ProducerRoutes extends RouteBuilder {

  // Producers

  @Autowired private StockTicksProducer stockTickProducerAvro;

  @Autowired private StockTicksConsumer stockTickConsumerAvro;

  /** {@inheritDoc} */
  @Override
  public void configure() throws Exception {

    // ALPHAVANYAGE REST API ==> KAFKA (ticks, 1Min, 1hour)
    from("timer:ticks?fixedRate=true&period={{alphavantage.pollingPeriod.ticks}}")
        .routeId("ticks_Alpha2Kafka")
        .process(stockTickProducerAvro)
        .end();

    // ALPHAVANYAGE KAFKA => DSE (TMP)
    from("timer:ticksConsumer?" + "fixedRate=true" + "&period={{alphavantage.pollingPeriod.ticks}}")
        .routeId("ticks_Kafka2Dse")
        .process(stockTickConsumerAvro)
        .end();
  }
}
