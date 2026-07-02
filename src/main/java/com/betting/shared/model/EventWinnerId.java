package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import lombok.NonNull;

public record EventWinnerId(@NonNull UUID value) implements Serializable {

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static EventWinnerId of(@NonNull String value) {
    return new EventWinnerId(UUID.fromString(value));
  }

  public static EventWinnerId random() {
    return new EventWinnerId(UUID.randomUUID());
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toString();
  }
}
