package com.betting.core.mock;

import com.betting.core.Bet;
import com.betting.core.BetRepository;
import com.betting.core.BetStatus;
import com.betting.shared.model.BetAmount;
import com.betting.shared.model.BetId;
import com.betting.shared.model.EventId;
import com.betting.shared.model.EventMarketId;
import com.betting.shared.model.EventWinnerId;
import com.betting.shared.model.UserId;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryDataLoader implements CommandLineRunner {

  private final BetRepository repository;

  @Override
  public void run(String... args) {

    List<Bet> bets = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Bet bet =
          Bet.builder()
              .betId(BetId.random())
              .userId(UserId.random())
              .eventId(EventId.of("2eff1484-b51a-4794-a0c9-abb1723afdd4"))
              .eventMarketId(EventMarketId.random())
              .predictedWinnerId(EventWinnerId.of("3f0942a4-519c-435c-a3bc-e9144d4c3df2"))
              .betAmount(BetAmount.of(BigDecimal.valueOf(100)))
              .betStatus(BetStatus.ACCEPTED)
              .build();

      bets.add(bet);
    }

    repository.saveAll(bets);

    log.debug("Mock Bets has been loaded into the system, {}", bets);
  }
}
