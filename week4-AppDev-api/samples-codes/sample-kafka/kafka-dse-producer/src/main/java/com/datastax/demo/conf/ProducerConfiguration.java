package com.datastax.demo.conf;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

  @Value("${kafka.server}")
  private String kafkaServer;

  @Value("${kafka.ack}")
  private String producerAck;

  @Value("${kafka.group}")
  private String consumerGroup;

  @Bean("producer.json")
  public KafkaProducer<String, JsonNode> jsonProducer() {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
    props.put(ACKS_CONFIG, producerAck);
    return new KafkaProducer<String, JsonNode>(props);
  }

  @Bean("consumer.json")
  public KafkaConsumer<String, JsonNode> jsonConsumer() {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    props.put(GROUP_ID_CONFIG, consumerGroup);
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
    return new KafkaConsumer<String, JsonNode>(props);
  }
}
