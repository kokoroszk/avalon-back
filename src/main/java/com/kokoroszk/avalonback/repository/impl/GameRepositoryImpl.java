package com.kokoroszk.avalonback.repository.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.stereotype.Repository;

import com.kokoroszk.avalonback.dto.Game;
import com.kokoroszk.avalonback.repository.GameRepository;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private Queue<Long> availableIds = new ConcurrentLinkedQueue<>(LongStream.range(0, 10000).boxed().collect(Collectors.toSet()));;
    private HashMap<Long, Game> repository = new HashMap<>();

    @Override
    public Optional<Game> get(long gameId) {
        return Optional.ofNullable(repository.get(gameId));
    }

    @Override
    public Collection<Game> getAllGame() {
        this.cleanUp();
        return repository.values();
    }

    @Override
    public Long getUniqueGameId() {
        return availableIds.poll();
    }

    synchronized private void cleanUp() {
        var expirationTime = LocalDateTime.now().minusMinutes(30L);
        Predicate<Entry<Long, Game>> isExpired = e -> e.getValue().getLastUpdateTime().isBefore(expirationTime);
        Consumer<Long> cleanUp = id -> {
            repository.remove(id);
            availableIds.offer(id);
        };

        repository.entrySet().stream().filter(isExpired).map(Entry::getKey).forEach(cleanUp);
    }

    @Override
    public void save(Game game) {
        this.repository.put(game.getGameId(), game);
    }
}
