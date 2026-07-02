package com.betting.shared.model;

import com.betting.shared.rocketmq.RocketMQMessage;
import lombok.Builder;

@Builder
public record BetSettlementMessage(BetId betId, EventWinnerId eventWinnerId)
    implements RocketMQMessage {}
