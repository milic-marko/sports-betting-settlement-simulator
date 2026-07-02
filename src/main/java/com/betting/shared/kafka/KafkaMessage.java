package com.betting.shared.kafka;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jspecify.annotations.NonNull;

public interface KafkaMessage {

  @NonNull
  @JsonIgnore
  String getKey();
}
