package com.kokoroszk.avalonback.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.kokoroszk.avalonback.code.Mission;
import com.kokoroszk.avalonback.code.Role;
import com.kokoroszk.avalonback.dto.Game;
import com.kokoroszk.avalonback.dto.Game.Player;
import com.kokoroszk.avalonback.repository.GameRepository;
import com.kokoroszk.avalonback.service.GameService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Game createNewGame(String gameName, List<String> playerNames, List<Role> additionalRoles) {
        // 他のゲームと重複しないIDで新規ゲームを生成する
        var gameId = this.gameRepository.getUniqueGameId();
        return this.createGame(gameId, gameName, playerNames, additionalRoles);
    }

    @Override
    public Collection<Game> getAllGame() {
        return this.gameRepository.getAllGame().stream()
                .sorted(Comparator.comparing(Game::getLastUpdateTime).reversed())
                .collect(toList());
    }

    @Override
    public Game getGame(long gameId, long operationCount) {
        Game game;
        int i = 0;
        do {
            try {
                if (i != 0)
                    Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game = this.gameRepository.get(gameId).orElseThrow();
        } while (++i < 5 * 30 /* max 30sec */ && game.getOperationCount().longValue() == operationCount);
        return game;
    }

    /**
     * ゲームを初期化してリスタートする。
     */
    @Override
    public Game resetGame(long gameId) {
        var oldGame = this.gameRepository.get(gameId).orElseThrow();
        var gameName = oldGame.getGameName();
        var playerNames = oldGame.getPlayers().stream().map(Player::getName).collect(toList());
        var additionalRoles = oldGame.getPlayers().stream()
                .map(Player::getRole).filter(Role::isAdditional).collect(toList());

        var newGame = this.createGame(gameId, gameName, playerNames, additionalRoles);
        return newGame;
    }

    private Game createGame(long gameId, String gameName, List<String> playerNames, List<Role> additionalRoles) {

        var playerCount = playerNames.size();
        var missions = this.getMissions(playerCount);
        var roles = this.getRoles(playerCount, additionalRoles);

        var game = new Game(gameId, gameName, playerNames, roles, missions);
        this.gameRepository.save(game);
        return game;
    }

    private List<Mission> getMissions(int playerCount) {
        // TODO Missions marked with an asterisk (*) require at least two fail cards
        switch (playerCount) {
        case 5:
            return Arrays.asList(Mission.RequireTwo, Mission.RequireThree, Mission.RequireTwo, Mission.RequireThree, Mission.RequireThree);
        case 6:
            return Arrays.asList(Mission.RequireTwo, Mission.RequireThree, Mission.RequireFour, Mission.RequireThree, Mission.RequireFour);
//      case 7:
//          return Arrays.asList(Mission.RequireTwo, Mission.RequireThree, Mission.RequireThree, Mission.RequireFour*, Mission.RequireFour);
//      case 8:
//          return Arrays.asList(Mission.RequireThree, Mission.RequireFour, Mission.RequireFour, Mission.RequireFive*, Mission.RequireFive);
//      case 9:
//          return Arrays.asList(Mission.RequireThree, Mission.RequireFour, Mission.RequireFour, Mission.RequireFive*, Mission.RequireFive);
//      case 10:
//          return Arrays.asList(Mission.RequireThree, Mission.RequireFour, Mission.RequireFour, Mission.RequireFive*, Mission.RequireFive);
        default:
            throw new IllegalArgumentException("プレイヤー数が不正です。");
        }
    }

    private List<Role> getRoles(int playerCount, List<Role> additionalRoles) {

        // r = resistance, e = evil
        int rCount = this.getResistanceRoleCount(playerCount);
        int eCount = playerCount - rCount;

        var rAdditions = additionalRoles.stream().filter(role -> role.isResistance()).collect(toList());
        var eAdditions = additionalRoles.stream().filter(role -> !role.isResistance()).collect(toList());

        int rLimit = rCount - rAdditions.size();
        int eLimit = eCount - eAdditions.size();
        var r = Stream.concat(rAdditions.stream(), Stream.generate(() -> Role.ArthurianKnight).limit(rLimit));
        var e = Stream.concat(eAdditions.stream(), Stream.generate(() -> Role.MinionOfMordred).limit(eLimit));

        return Stream.concat(r, e).collect(toList());
    }

    private int getResistanceRoleCount(int playerCount) {
        switch (playerCount) {
        case 5:
            return 3;
        case 6:
            return 4;
        case 7:
            return 4;
        case 8:
            return 5;
        case 9:
            return 6;
        case 10:
            return 6;
        default:
            throw new IllegalArgumentException("プレイヤー数が不正です。");
        }
    }
}
