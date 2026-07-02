package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import lombok.NonNull;

public record EventId(@NonNull UUID value) implements Serializable {

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static EventId of(@NonNull String value) {
    return new EventId(UUID.fromString(value));
  }

  public static EventId random() {
    return new EventId(UUID.randomUUID());
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toString();
  }
}
