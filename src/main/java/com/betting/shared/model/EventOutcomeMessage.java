package com.betting.shared.model;

import com.betting.shared.kafka.KafkaMessage;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder
public record EventOutcomeMessage(EventId eventId, EventWinnerId eventWinnerId)
    implements KafkaMessage {

  @Override
  public @NonNull String getKey() {
    return eventId.toString();
  }
}
