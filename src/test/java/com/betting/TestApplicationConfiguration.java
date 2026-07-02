package com.betting;

import com.betting.KafkaTestHelper.KafkaTestConsumer;
import com.betting.KafkaTestHelper.KafkaTestProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@TestConfiguration
class TestApplicationConfiguration {

  @Bean
  KafkaTestProducer kafkaTestProducer(KafkaProperties properties) {
    return KafkaTestProducer.builder()
        .bootstrapServers(properties.getBootstrapServers().getFirst())
        .keySerializer(StringSerializer.class)
        .valueSerializer(JacksonJsonSerializer.class)
        .build();
  }

  @Bean
  KafkaTestConsumer kafkaTestConsumer(KafkaProperties properties) {
    return KafkaTestConsumer.builder()
        .bootstrapServers(properties.getBootstrapServers().getFirst())
        .keyDeserializer(StringDeserializer.class)
        .valueDeserializer(JacksonJsonDeserializer.class)
        .build();
  }
}
