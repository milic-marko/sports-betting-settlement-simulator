package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import lombok.NonNull;

public record BetId(@NonNull UUID value) implements Serializable {

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static BetId of(@NonNull String value) {
    return new BetId(UUID.fromString(value));
  }

  public static BetId random() {
    return new BetId(UUID.randomUUID());
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toString();
  }
}
