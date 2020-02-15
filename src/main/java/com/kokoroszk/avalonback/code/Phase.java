package com.kokoroszk.avalonback.code;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ゲームのフェーズを示す。
 */
public enum Phase {

    /** リーダーが次のミッションに行くプレイヤーを選択中 */
    MemberSelection(1),

    /** 各プレイヤーがミッション参加者の承認/却下を投票中 */
    Vote(2),

    /** 他プレイヤーの投票を待機中 */
    WaitVote(3),

    /** ミッション参加者として成功/失敗を選択中 */
    Mission(4),

    /** 他プレイヤーのミッション成功/失敗の選択を待機中 */
    WaitMission(5);

    private int code;

    /** コードからEnumに変換するためのMap */
    private static final Map<Integer, Phase> codeToEnum = Stream.of(Phase.values())
            .collect(Collectors.toMap(Phase::getCode, Function.identity()));

    private Phase(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return this.code;
    }

    public static Optional<Phase> codeOf(int code) {
        return Optional.of(codeToEnum.get(code));
    }
}
