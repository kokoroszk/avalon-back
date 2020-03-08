package com.kokoroszk.avalonback.response;

import java.util.List;

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
    private Long playerId;

    /** ゲームの名称 */
    private String gameName;

    /** 参加中のプレイヤーリスト */
    private List<Player> players;

    /** 参加人数に応じたミッションのリスト */
    private List<Mission> missions;

    /** 現在のミッションのインデックス */
    private Integer currentMission;

    /** 現在の却下回数 */
    private Integer rejectionCount;

    /** 直前のミッション結果 */
    private List<Boolean> lastMissionResult;

    /** 現在のフェーズ */
    private Phase phase;

    /** 現在のリーダプレイヤーのID */
    private Integer leaderId;

    /** Gameに対する操作回数。更新確認で利用。 */
    private long operationCount;

    /**
     * プレイヤーの情報。
     */
    @JsonInclude(content = Include.NON_EMPTY)
    @Setter
    @Getter
    public static class Player {

        /** プレイヤーID */
        private Integer id;

        /** プレイヤーの名前 */
        private String name;

        /** ミッション参加者として選択されたか */
        private Boolean isSelected;

        /** 直前の投票内容 */
        private Vote vote;

        /** 役職 */
        private Role role;
    }
}
