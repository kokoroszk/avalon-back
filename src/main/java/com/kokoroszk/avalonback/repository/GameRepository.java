package com.kokoroszk.avalonback.repository;

import java.util.Collection;
import java.util.Optional;

import com.kokoroszk.avalonback.dto.Game;

/**
 * 実行中のゲームを管理するリポジトリ。
 */
public interface GameRepository {

	public Optional<Game> get(long gameId);

	public Collection<Game> getAllGame();

    public Long getUniqueGameId();

    public void save(Game game);
}
