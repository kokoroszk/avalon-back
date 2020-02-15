package com.kokoroszk.avalonback.service;

import java.util.Collection;

import com.kokoroszk.avalonback.code.Vote;

/**
 * ゲームプレイに関連するサービスクラス。
 */
public interface GamePlayingService {

    /**
     * ミッションを行うプレイヤーを選択する。
     * @param selectedPlayerIds 選択されたプレイヤー
     */
    void selectMembers(long gameId, Collection<Integer> selectedPlayerIds);

    /**
     * 投票を行う。
     * 
     * @param gameId プレイ中のゲームID
     * @param playerId プレイヤーID
     * @param vote   承認または却下。
     * @return 投票を反映したゲームオブジェクト。
     */
    void vote(long gameId, int playerId, Vote vote);

    /**
     * ミッションを行う。
     * @param gameId プレイ中のゲームID
     * @param isSuccess 成功または失敗。
     * @return ミッションを反映したゲームオブジェクト。
     */
    void playMission(long gameId, boolean isSuccess);
}
