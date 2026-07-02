package com.betting.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import lombok.NonNull;

public record BetAmount(BigDecimal value) {
  public BetAmount {
    if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Bet amount must be positive");
    }
  }

  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static BetAmount of(@NonNull BigDecimal value) {
    return new BetAmount(value);
  }

  @JsonValue
  @Override
  public String toString() {
    return value.toPlainString();
  }
}
