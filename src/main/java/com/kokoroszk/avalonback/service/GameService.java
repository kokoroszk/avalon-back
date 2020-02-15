package com.kokoroszk.avalonback.service;

import java.util.Collection;
import java.util.List;

import com.kokoroszk.avalonback.code.Role;
import com.kokoroszk.avalonback.dto.Game;

/**
 * ゲームオブジェクトに関連するサービスクラス。
 */
public interface GameService {

    /**
     * 新規ゲームを作成する。
     * 
     * @param ゲームの名称
     * @param プレイヤー名
     * @return 生成された新規ゲーム。
     */
    public Game createNewGame(String gameName, List<String> playerNames, List<Role> additionRoles);

    /**
     * 全てゲームを取得する。
     * 
     * @return ゲームのリスト
     */
    public Collection<Game> getAllGame();

    /**
     * ゲームを取得する。フェーズに変更がない場合、一定期間変更を待機する。
     * 
     * @param gameId 取得するゲームID
     * @return ゲームオブジェクト
     */
    public Game getGame(long gameId, long operationCount);

    /**
     * ゲームを初期化する
     * 
     * @param gameId 初期化するゲームID
     * @return 初期化したゲームオブジェクト
     */
    public Game resetGame(long gameId);

}
