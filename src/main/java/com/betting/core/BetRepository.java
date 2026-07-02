package com.betting.core;

import com.betting.shared.model.BetId;
import com.betting.shared.model.EventId;
import java.util.List;
import java.util.Optional;

public interface BetRepository {

  Bet save(Bet bet);

  List<Bet> saveAll(List<Bet> bets);

  boolean existsByEventId(EventId eventId);

  Optional<Bet> findById(BetId betId);

  List<Bet> findAllByEventId(EventId eventId);
}
