package com.betting.shared.kafka;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaStreamRouter {

  private final StreamBridge stream;

  public void send(@NonNull String binding, @NonNull KafkaMessage event) {
    log.debug("Sending record to Kafka binding [ {} ] - Content: [ {} ]", binding, event);

    try {
      Message<KafkaMessage> message =
          MessageBuilder.withPayload(event)
              .setHeader(KafkaHeaders.KEY, event.getKey())
              .build();

      boolean sent = stream.send(binding, message);

      if (!sent) {
        throw new StreamRouterException("Can not send message to output binding");
      }
    } catch (Exception error) {
      log.error(
          "Failed to send message to binding [ {} ]. Error: {}",
          binding,
          error.getMessage(),
          error);
      throw new StreamRouterException(error);
    }
  }
}
