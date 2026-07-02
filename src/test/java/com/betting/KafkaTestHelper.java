package com.betting;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Log4j2
public class KafkaTestHelper {

  private KafkaTestHelper() {}

  public static class KafkaTestConsumer {
    private final String bootstrapServers;
    private final Class<? extends Deserializer> keyDeserializer;
    private final Class<? extends Deserializer> valueDeserializer;

    private KafkaTestConsumer(
        @NonNull String bootstrapServers,
        @NonNull Class<? extends Deserializer> keyDeserializer,
        @NonNull Class<? extends Deserializer> valueDeserializer) {
      this.bootstrapServers = bootstrapServers;
      this.keyDeserializer = keyDeserializer;
      this.valueDeserializer = valueDeserializer;
    }

    public <K, V> Optional<ConsumerRecord<K, V>> getRecords(
        Consumer<K, V> consumer,
        String topic,
        Duration timeout,
        Predicate<ConsumerRecord<K, V>> predicate) {

      long expire = System.currentTimeMillis() + timeout.toMillis();
      while (System.currentTimeMillis() < expire) {
        ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<K, V> record : records.records(topic)) {
          if (predicate.test(record)) {
            return Optional.of(record);
          }
        }
      }
      return Optional.empty();
    }

    public <Key, Value> Optional<ConsumerRecord<Key, Value>> consumeOne(
        @NonNull String topic,
        @NonNull Duration timeout,
        @NonNull Predicate<ConsumerRecord<Key, Value>> predicate) {
      return getRecords(createConsumer(topic), topic, timeout, predicate);
    }

    private <Key, Value> Consumer<Key, Value> createConsumer(String topic) {
      DefaultKafkaConsumerFactory<Key, Value> factory =
          new DefaultKafkaConsumerFactory<>(consumerProperties());
      Consumer<Key, Value> consumer = factory.createConsumer();
      consumer.subscribe(Collections.singletonList(topic));

      return consumer;
    }

    private Map<String, Object> consumerProperties() {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      properties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
      properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
      properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
      properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
      properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1);
      properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
      properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 100);
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
      properties.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
      properties.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, true);
      return properties;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static class Builder {
      private String bootstrapServers;
      private Class<? extends Deserializer> keyDeserializer = StringDeserializer.class;
      private Class<? extends Deserializer> valueDeserializer = StringDeserializer.class;

      public Builder bootstrapServers(@NonNull String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        return this;
      }

      public Builder keyDeserializer(@NonNull Class<? extends Deserializer> serializer) {
        this.keyDeserializer = serializer;
        return this;
      }

      public Builder valueDeserializer(@NonNull Class<? extends Deserializer> deserializer) {
        this.valueDeserializer = deserializer;
        return this;
      }

      public KafkaTestConsumer build() {
        return new KafkaTestConsumer(bootstrapServers, keyDeserializer, valueDeserializer);
      }
    }
  }

  public static class KafkaTestProducer {
    private final String bootstrapServers;
    private final Class<? extends Serializer> keySerializer;
    private final Class<? extends Serializer> valueSerializer;

    private KafkaTestProducer(
        @NonNull String bootstrapServers,
        @NonNull Class<? extends Serializer> keySerializer,
        @NonNull Class<? extends Serializer> valueSerializer) {
      this.bootstrapServers = bootstrapServers;
      this.keySerializer = keySerializer;
      this.valueSerializer = valueSerializer;
    }

    public <Key, Value> void send(@NonNull String topic, @NonNull Key key, @NonNull Value value) {
      try (KafkaProducer<Key, Value> producer = createProducer()) {
        ProducerRecord<Key, Value> record = new ProducerRecord<>(topic, key, value);
        producer.send(record);
        log.debug(
            "Record sent, topic: [ {} ], key: [ {} ], value: [ {} ]",
            topic,
            record.key(),
            record.value());
      }
    }

    private <Key, Value> KafkaProducer<Key, Value> createProducer() {
      return new KafkaProducer<>(producerProperties());
    }

    private Map<String, Object> producerProperties() {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
      properties.put(ProducerConfig.LINGER_MS_CONFIG, 0);
      properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);

      return properties;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static class Builder {
      private String bootstrapServers;
      private Class<? extends Serializer> keySerializer = StringSerializer.class;
      private Class<? extends Serializer> valueSerializer = StringSerializer.class;

      public Builder bootstrapServers(@NonNull String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        return this;
      }

      public Builder keySerializer(@NonNull Class<? extends Serializer> serializer) {
        this.keySerializer = serializer;
        return this;
      }

      public Builder valueSerializer(@NonNull Class<? extends Serializer> serializer) {
        this.valueSerializer = serializer;
        return this;
      }

      public KafkaTestProducer build() {
        return new KafkaTestProducer(bootstrapServers, keySerializer, valueSerializer);
      }
    }
  }
}
