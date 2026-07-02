package com.betting.entry;

import com.betting.IntegrationTest;
import com.betting.KafkaTestHelper.KafkaTestConsumer;
import com.betting.shared.APIPaths;
import com.betting.shared.KafkaTopics;
import com.betting.shared.model.EventId;
import com.betting.shared.model.EventOutcomeMessage;
import com.betting.shared.model.EventWinnerId;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

@IntegrationTest
class EventOutcomeControllerTest {

  public static final String API_PATH = APIPaths.EVENT_OUTCOMES;

  @Autowired private RestTestClient client;
  @Autowired private KafkaTestConsumer kafkaTestConsumer;

  @Test
  void should_publish_outcome_event_to_kafka() {
    // Given
    EventOutcomeRequest request =
        EventOutcomeRequest.builder()
            .eventId(EventId.random().toString())
            .eventWinnerId(EventWinnerId.random().toString())
            .build();

    // When
    ResponseSpec result = client.post().uri(API_PATH).body(request).exchange();

    // Then
    result.expectStatus().isAccepted();

    Optional<ConsumerRecord<String, EventOutcomeMessage>> objectObjectConsumerRecord =
        kafkaTestConsumer.consumeOne(
            KafkaTopics.EVENT_OUTCOMES,
            Duration.of(10, ChronoUnit.SECONDS),
            consumerRecord -> consumerRecord.value().toString().contains(request.eventId()));
    EventOutcomeMessage kafkaMessage = objectObjectConsumerRecord.get().value();

    Assertions.assertEquals(request.eventId(), kafkaMessage.eventId().toString());
    Assertions.assertEquals(request.eventWinnerId(), kafkaMessage.eventWinnerId().toString());
  }

  @Test
  void should_return_400_on_bad_input() {
    // Given
    EventOutcomeRequest request =
        EventOutcomeRequest.builder()
            .eventId("wrong-type")
            .eventWinnerId(EventWinnerId.random().toString())
            .build();

    // When
    ResponseSpec result = client.post().uri(API_PATH).body(request).exchange();

    // Then
    result.expectStatus().isBadRequest();
  }
}
