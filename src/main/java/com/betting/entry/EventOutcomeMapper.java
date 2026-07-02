package com.betting.entry;

import com.betting.shared.model.EventId;
import com.betting.shared.model.EventOutcomeMessage;
import com.betting.shared.model.EventWinnerId;

class EventOutcomeMapper {

  private EventOutcomeMapper() {}

  static EventOutcomeMessage toDomain(EventOutcomeRequest request) {
    return EventOutcomeMessage.builder()
        .eventId(EventId.of(request.eventId()))
        .eventWinnerId(EventWinnerId.of(request.eventWinnerId()))
        .build();
  }
}
