package com.betting.core.mock;

import com.betting.core.Bet;
import com.betting.core.BetRepository;
import com.betting.shared.model.BetId;
import com.betting.shared.model.EventId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class InMemoryBetRepository implements BetRepository {

  private final Map<EventId, List<Bet>> eventIdBetMap = new HashMap<>();

  @Override
  public Bet save(Bet bet) {
    List<Bet> bets = eventIdBetMap.computeIfAbsent(bet.getEventId(), k -> new ArrayList<>());

    bets.removeIf(b -> b.getBetId().equals(bet.getBetId()));
    bets.add(bet);

    return bet;
  }

  @Override
  public List<Bet> saveAll(List<Bet> bets) {
    bets.forEach(this::save);

    return bets;
  }

  @Override
  public boolean existsByEventId(EventId eventId) {
    List<Bet> bets = eventIdBetMap.get(eventId);
    return bets != null && !bets.isEmpty();
  }

  @Override
  public Optional<Bet> findById(BetId betId) {
    return eventIdBetMap.values().stream()
        .flatMap(List::stream)
        .filter(bet -> bet.getBetId().equals(betId))
        .findFirst();
  }

  @Override
  public List<Bet> findAllByEventId(EventId eventId) {
    return eventIdBetMap.getOrDefault(eventId, List.of());
  }
}
