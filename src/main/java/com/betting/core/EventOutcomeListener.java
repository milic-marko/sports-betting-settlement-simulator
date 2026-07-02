package com.betting.core;

import com.betting.shared.model.EventOutcomeMessage;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EventOutcomeListener {

  private final BetMatchingService betMatchingService;

  @Bean
  Consumer<EventOutcomeMessage> onEventOutcome() {
    return betMatchingService::matchBets;
  }
}
