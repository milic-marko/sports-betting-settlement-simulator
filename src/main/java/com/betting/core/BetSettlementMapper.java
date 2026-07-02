package com.betting.core;

import com.betting.shared.model.BetSettlementMessage;
import com.betting.shared.model.EventWinnerId;
import java.util.List;

class BetSettlementMapper {

  private BetSettlementMapper() {}

  public static List<BetSettlementMessage> toDto(List<Bet> bets, EventWinnerId eventWinnerId) {
    return bets.stream()
        .map(
            bet ->
                BetSettlementMessage.builder()
                    .betId(bet.getBetId())
                    .eventWinnerId(eventWinnerId)
                    .build())
        .toList();
  }
}
