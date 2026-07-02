package com.betting.core;

import com.betting.shared.RocketMQTopics;
import com.betting.shared.model.BetSettlementMessage;
import com.betting.shared.model.EventOutcomeMessage;
import com.betting.shared.rocketmq.RocketMQStreamRouter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class BetMatchingService {

  private final BetRepository repository;
  private final RocketMQStreamRouter rocketMQStreamRouter;

  public void matchBets(EventOutcomeMessage message) {

    // we should deduplicate events

    if (!repository.existsByEventId(message.eventId())) {
      log.info("There's no matching bets for eventId: [ {} ]", message.eventId());
      return;
    }

    List<Bet> bets = repository.findAllByEventId(message.eventId());

    List<BetSettlementMessage> settlementMessages =
        BetSettlementMapper.toDto(bets, message.eventWinnerId());

    log.info("Found {} bets for eventId: [ {} ]", bets.size(), message.eventId());

    // publish to rocketMQ
    // what if publishing fails, how to ensure event is not lost(outbox pattern) depending on amount
    // of traffic?
    settlementMessages.forEach(
        settlementMessage ->
            rocketMQStreamRouter.send(RocketMQTopics.BET_SETTLEMENTS, settlementMessage));
  }
}
