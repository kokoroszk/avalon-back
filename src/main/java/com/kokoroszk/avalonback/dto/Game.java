package com.kokoroszk.avalonback.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.kokoroszk.avalonback.code.Mission;
import com.kokoroszk.avalonback.code.Phase;
import com.kokoroszk.avalonback.code.Role;
import com.kokoroszk.avalonback.code.Vote;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ゲーム状態を表すオブジェクト。
 */
@Getter
@Setter
@EqualsAndHashCode
public class Game {

    /** ゲームID */
    private long gameId;

    /** ゲームの名称 */
    private String gameName;

    /** 参加中のプレイヤーリスト */
    private List<Player> players;

    /** 現在のリーダプレイヤーのID */
    private int leaderId;

    /** 現在のフェーズ */
    private Phase phase;

    /** 参加人数に応じたミッションのリスト */
    private List<Mission> missions;

    /** 現在のミッションのインデックス */
    private int currentMission;

    /** 現在の却下回数 */
    private int rejectionCount;

    /** 直前のミッション結果 */
    private List<Boolean> lastMissionResult;

    /** Gameに対する操作回数。更新確認で利用。 */
    private AtomicLong operationCount;

    /** 最終更新日時。オブジェクトの破棄タイミング管理で利用。 */
    private LocalDateTime lastUpdateTime;

    /** このフィールドはゲーム進行中に更新せず、ミッションの必要人数を保存しておく。リスタートで利用。 */
    private List<Mission> initialMissions;

    /**
     * プレイヤーの情報。
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Player {

        /** プレイヤーID */
        private int id;

        /** プレイヤーの名前 */
        private String name;

        /** ミッション参加者として選択されているか */
        private boolean isSelected;

        /** 直前の投票内容 */
        private Vote vote;

        /** 役職 */
        private Role role;

        private Player(int id, String name, Role role) {
            this.id = id;
            this.name = name;;
            this.isSelected = false;
            this.vote = Vote.None;
            this.role = role;
        }
    }

    public Game(long gameId, String gameName, List<String> playerNames, List<Role> roles, List<Mission> missions) {

        // 役職がランダムに設定されることを保証する。
        Collections.shuffle(roles);
        var players = IntStream.range(0, playerNames.size())
                .mapToObj(i -> new Player(i, playerNames.get(i), roles.get(i)))
                .collect(Collectors.toList());

        this.gameId = gameId;
        this.gameName = gameName;
        this.players = players;
        this.leaderId = ThreadLocalRandom.current().nextInt(playerNames.size());
        this.phase = Phase.MemberSelection;
        this.missions = missions;
        this.currentMission = 0;
        this.rejectionCount = 0;
        this.lastMissionResult = Collections.emptyList();
        this.operationCount = new AtomicLong(0);
        this.lastUpdateTime = LocalDateTime.now();
        this.initialMissions = missions;
    }
}
