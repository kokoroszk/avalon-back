package com.kokoroszk.avalonback.service.impl;

import static java.util.Comparator.comparing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.kokoroszk.avalonback.code.Mission;
import com.kokoroszk.avalonback.code.Phase;
import com.kokoroszk.avalonback.code.Vote;
import com.kokoroszk.avalonback.dto.Game;
import com.kokoroszk.avalonback.dto.Game.Player;
import com.kokoroszk.avalonback.repository.GameRepository;
import com.kokoroszk.avalonback.service.GamePlayingService;

import lombok.RequiredArgsConstructor;

/**
 * ゲーム進行に関するサービスクラス。<br>
 * フェーズは MemberSelect <--> Vote -> Mission -> MemberSelect -> ... と進行する。
 */
@Service
@RequiredArgsConstructor
public class GamePlayingServiceImpl implements GamePlayingService {

    private final GameRepository gameRepository;

    /**
     * リーダプレイヤーはミッションを行うプレイヤーを選択して、Voteフェーズに移行する。
     */
    @Override
    public void selectMembers(long gameId, Collection<Integer> selectedPlayerIds) {
        var game = this.gameRepository.get(gameId).orElseThrow();
        game.getPlayers().stream()
                .filter(p -> selectedPlayerIds.contains(p.getId()))
                .forEach(p -> p.setSelected(true));
        this.forwardToVote(game);
    }

    /**
     * 選択されたプレイヤーがミッションを行うことに対して 承認/却下 で投票を行う。<br>
     * 過半数のプレイヤーが承認した場合、Missionフェーズに移行する。<br>
     * 半数以上のプレイヤーが却下した場合、リーダプレイヤーを変更して、再度 MemberSelectフェーズを行う。
     */
    @Override
    public void vote(long gameId, int playerId, Vote vote) {
        var game = this.gameRepository.get(gameId).orElseThrow();
        game.getPlayers().stream().filter(p -> p.getId() == playerId).forEach(p -> p.setVote(vote));
        this.updateGame(game);

        // TODO 却下が5回行われた場合に Evil side の勝利となる判定が未実装
        boolean isVoteFinished = game.getPlayers().stream().allMatch(p -> p.getVote() != Vote.None);
        if (isVoteFinished) {
            long accepted = game.getPlayers().stream().filter(p -> p.getVote() == Vote.Accept).count();
            long rejected = game.getPlayers().stream().filter(p -> p.getVote() == Vote.Reject).count();
            boolean isAccepted = accepted > rejected;
            if (isAccepted) {
                this.forwardToMission(game);
            } else {
                game.setRejectionCount(game.getRejectionCount() + 1);
                this.forwardToMemberSelect(game);
            }
        }
    }

    /**
     * 選択されたプレイヤーがミッションの 成功/失敗 を選択する。<br>
     * 成功のみ選択された場合はミッション成功とする。<br>
     * 失敗が含まれていた場合はミッション失敗とする。<br>
     * 成功/失敗に関わらず、リーダプレイヤーを変更して、MemberSelectフェーズに移行する。
     */
    @Override
    public void playMission(long gameId, boolean isSuccess) {
        var game = this.gameRepository.get(gameId).orElseThrow();
        game.getLastMissionResult().add(isSuccess);
        this.updateGame(game);

        // ミッション終了判定
        int requiredCount = game.getMissions().get(game.getCurrentMission()).getCode();
        if (game.getLastMissionResult().size() == requiredCount) {
            game.getLastMissionResult().sort((b1, b2) -> b1 && b2 ? 0 : b1 ? -1 : 1);

            // TODO ミッション失敗の為に、失敗が2つ要求されるケースが未実装
            // TODO goodまたはevilが3回ミッションに成功した場合に勝利となる判定が未実装
            var result = (game.getLastMissionResult().stream().allMatch(b -> b))
                    ? Mission.AurthurWin
                    : Mission.MordretWin;
            game.getMissions().set(game.getCurrentMission(), result);
            game.setCurrentMission(game.getCurrentMission() + 1);
            this.forwardToMemberSelect(game);
        }
    }

    /**
     * Voteフェーズに移行する。
     */
    private void forwardToVote(Game game) {
        game.getPlayers().stream().forEach(p -> p.setVote(Vote.None));
        game.setPhase(Phase.Vote);
        this.updateGame(game);
    }

    /**
     * Missionフェーズに移行する。
     */
    private void forwardToMission(Game game) {
        game.setLastMissionResult(new ArrayList<>());
        game.setPhase(Phase.Mission);
        this.updateGame(game);
    }

    /**
     * MemberSelectionフェーズに移行する。
     */
    private void forwardToMemberSelect(Game game) {
        this.changeLeader(game);
        game.getPlayers().stream().forEach(p -> p.setSelected(false));
        game.setPhase(Phase.MemberSelection);
        this.updateGame(game);
    }

    /**
     * リーダプレイヤーを変更する。
     */
    private void changeLeader(Game game) {

        Supplier<Player> minIdPalyer = () -> game.getPlayers().stream().min(comparing(Player::getId)).orElseThrow();
        var nextLeader = game.getPlayers().stream()
                .filter(p -> p.getId() > game.getLeaderId())
                .findFirst()
                .orElseGet(minIdPalyer);

        game.setLeaderId(nextLeader.getId());
    }

    /**
     * Gameオブジェクトの更新回数と最終更新日時を更新する。
     */
    private void updateGame(Game game) {
        game.getOperationCount().incrementAndGet();
        game.setLastUpdateTime(LocalDateTime.now());
    }
}
