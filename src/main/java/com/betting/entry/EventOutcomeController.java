package com.betting.entry;

import com.betting.shared.APIPaths;
import com.betting.shared.KafkaTopics;
import com.betting.shared.kafka.KafkaStreamRouter;
import com.betting.shared.model.EventOutcomeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(APIPaths.EVENT_OUTCOMES)
class EventOutcomeController {

  private final KafkaStreamRouter kafkaStreamRouter;

  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  void publishOutcome(@Valid @RequestBody EventOutcomeRequest request) {
    EventOutcomeMessage message = EventOutcomeMapper.toDomain(request);
    kafkaStreamRouter.send(KafkaTopics.EVENT_OUTCOMES, message);
  }
}
