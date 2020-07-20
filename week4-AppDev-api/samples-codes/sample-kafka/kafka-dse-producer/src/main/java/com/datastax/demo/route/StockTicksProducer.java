package com.datastax.demo.route;

import com.datastax.demo.dao.AlphaVantageDao;
import com.datastax.demo.dao.DseDao;
import com.datastax.demo.dao.KafkaDao;
import com.datastax.demo.domain.StockTick;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Read Quotes values in RT and push to KAFKA. */
@Component("stockTicks.producer")
public class StockTicksProducer implements Processor {

  /** Json Jackson parser. */
  protected static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

  /** Internal logger. */
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass().getName());

  @Autowired protected DseDao dseDao;

  @Autowired protected AlphaVantageDao alphaVantageDao;

  @Autowired protected KafkaDao kafkaDao;

  @Value("${alphavantage.waitTime: 100 }")
  protected int apiWaitTime;

  @Value("${kafka.topics.ticks}")
  private String topicTicks;

  /** Symbols in CSV FILE. */
  protected Set<String> symbols;

  /** Askl to get prices. */
  protected Map<String, StockTick> initialStockPrices = null;

  /** Initialize connection to API AlphaVantage */
  @PostConstruct
  public void init() {
    symbols = dseDao.getSymbolsNYSE();
    LOGGER.info("Symbols list retrieved from DSE. ({} items)", symbols.size());

    initialStockPrices =
        alphaVantageDao
            .getCurrentStockTicks(symbols)
            .collect(Collectors.toMap(StockTick::getSymbol, Function.identity()));
    LOGGER.info("Stocks initial prices retrieved from alphaVantage REST API.");
  }

  /** {@inheritDoc} */
  @Override
  public void process(Exchange exchange) throws Exception {
    LOGGER.info(
        "Pushing '{}' stocks ticks to Kafka topic '{}'", initialStockPrices.size(), topicTicks);
    initialStockPrices
        .values()
        .stream()
        // Map to Avro Message
        .map(this::mapAsProducerRecord)
        // Send to Kafka
        .forEach(kafkaDao::sendJsonMessage);
  }

  public ProducerRecord<String, JsonNode> mapAsProducerRecord(StockTick sTick) {
    sTick.setValue(createRandomValue(sTick.getValue()));
    sTick.setValueDate(System.currentTimeMillis());
    JsonNode jsonValue = JACKSON_MAPPER.valueToTree(sTick);
    return new ProducerRecord<String, JsonNode>(topicTicks, sTick.getSymbol(), jsonValue);
  }

  /** Randomly making the stock evolving with random */
  private double createRandomValue(double lastValue) {
    double up = Math.random() * 2;
    double percentMove = (Math.random() * 1.0) / 100;
    if (up < 1) {
      lastValue -= percentMove * lastValue;
    } else {
      lastValue += percentMove * lastValue;
    }
    return lastValue;
  }
}
