package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import lombok.NonNull;

public record EventMarketId(@NonNull UUID value) implements Serializable {

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static EventMarketId of(@NonNull String value) {
    return new EventMarketId(UUID.fromString(value));
  }

  public static EventMarketId random() {
    return new EventMarketId(UUID.randomUUID());
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toString();
  }
}
