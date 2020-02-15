package com.kokoroszk.avalonback.response;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kokoroszk.avalonback.code.Mission;
import com.kokoroszk.avalonback.code.Phase;
import com.kokoroszk.avalonback.code.Role;
import com.kokoroszk.avalonback.code.Vote;

import lombok.Getter;
import lombok.Setter;

/**
 * クライアントに返却するゲーム状態を表すオブジェクト。
 */
@Getter
@Setter
@JsonInclude(content = Include.ALWAYS)
public class GameResponse {

    /** ゲームID */
    private Long gameId;

    /** リクエストされたプレイヤーのID */
    Long playerId;

    /** ゲームの名称 */
    String gameName;

    /** 参加中のプレイヤーリスト */
    List<Player> players;

    /** 参加人数に応じたミッションのリスト */
    List<Mission> missions;

    /** 現在のミッションのインデックス */
    Integer currentMission;

    /** 現在の却下回数 */
    Integer rejectionCount;

    /** 直前のミッション結果 */
    List<Boolean> lastMissionResult;

    /** 現在のフェーズ */
    Phase phase;

    /** 現在のリーダプレイヤーのID */
    Integer leaderId;

    /** Gameに対する操作回数。更新確認で利用。 */
    private AtomicLong operationCount;

    /**
     * プレイヤーの情報。
     */
    @JsonInclude(content = Include.NON_EMPTY)
    public static class Player {

        /** プレイヤーID */
        Integer id;

        /** プレイヤーの名前 */
        String name;

        /** ミッション参加者として選択されたか */
        Boolean isSelected;

        /** 直前の投票内容 */
        Vote vote;

        /** 役職 */
        Role role;
    }
}
