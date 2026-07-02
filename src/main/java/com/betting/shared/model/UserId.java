package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import lombok.NonNull;

public record UserId(@NonNull UUID value) implements Serializable {

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static UserId of(@NonNull String value) {
    return new UserId(UUID.fromString(value));
  }

  public static UserId random() {
    return new UserId(UUID.randomUUID());
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toString();
  }
}
