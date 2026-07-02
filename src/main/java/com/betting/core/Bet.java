package com.betting.core;

import com.betting.shared.model.BetAmount;
import com.betting.shared.model.BetId;
import com.betting.shared.model.EventId;
import com.betting.shared.model.EventMarketId;
import com.betting.shared.model.EventWinnerId;
import com.betting.shared.model.UserId;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.jspecify.annotations.Nullable;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class Bet {
  private final BetId betId;
  private final UserId userId;
  private final EventId eventId;
  private final EventMarketId eventMarketId;
  private final EventWinnerId predictedWinnerId;
  private final BetAmount betAmount;
  @With private final BetStatus betStatus;
  @With @Nullable private final BetResult betResult;

  public Bet settle(EventWinnerId actualWinnerId) {
    BetResult result =
        this.predictedWinnerId.equals(actualWinnerId) ? BetResult.WIN : BetResult.LOSE;

    return this.withBetResult(result).withBetStatus(BetStatus.SETTLED);
  }

  public boolean isSettled() {
    return betStatus.equals(BetStatus.SETTLED);
  }
}
