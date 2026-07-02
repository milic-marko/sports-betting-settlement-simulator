package com.betting.settlement;

import com.betting.shared.model.BetSettlementMessage;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BetSettlementListener {

  private final BetSettlementService betSettlementService;

  @Bean
  Consumer<BetSettlementMessage> onBetSettlement() {
    return betSettlementService::settleBet;
  }
}
