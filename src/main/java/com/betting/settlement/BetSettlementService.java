package com.betting.settlement;

import com.betting.core.Bet;
import com.betting.core.BetRepository;
import com.betting.shared.model.BetSettlementMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class BetSettlementService {

  private final BetRepository betRepository;

  void settleBet(BetSettlementMessage message) {

    // we should deduplicate events

    Optional<Bet> optionalBet = betRepository.findById(message.betId());
    if (optionalBet.isEmpty()) {
      log.info("Bet does not exist, betId: [ {} ]", message.betId());
      return;
    }

    Bet bet = optionalBet.get();
    if (bet.isSettled()) {
      log.info("Bet is already settled, skipping, betId: [ {} ]", bet.getBetId());
      return;
    }

    bet = bet.settle(message.eventWinnerId());
    betRepository.save(bet);

    log.info(
        "Bet has been settled, betId: [ {} ], result: [ {} ]", bet.getBetId(), bet.getBetResult());
  }
}
