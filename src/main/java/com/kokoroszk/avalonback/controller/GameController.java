package com.kokoroszk.avalonback.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kokoroszk.avalonback.code.Role;
import com.kokoroszk.avalonback.code.Vote;
import com.kokoroszk.avalonback.dto.Game;
import com.kokoroszk.avalonback.request.game.CreateRequest;
import com.kokoroszk.avalonback.request.game.MissionRequest;
import com.kokoroszk.avalonback.request.game.ResetRequest;
import com.kokoroszk.avalonback.request.game.SelectMembersRequest;
import com.kokoroszk.avalonback.request.game.VoteRequest;
import com.kokoroszk.avalonback.response.GameResponse;
import com.kokoroszk.avalonback.service.GamePlayingService;
import com.kokoroszk.avalonback.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final GamePlayingService gamePlayingService;

    @RequestMapping(path = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GameResponse> create(@RequestBody CreateRequest req) {

        List<Integer> roleCodes = ObjectUtils.defaultIfNull(req.getAdditionalRoles(), Collections.emptyList());
        var additionalRoles = roleCodes.stream().map(code -> Role.codeOf(code).orElseThrow()).collect(Collectors.toList());

        var game = this.gameService.createNewGame(req.getGameName(), req.getPlayerNames(), additionalRoles);
        var response = this.mapGameToResponse(game);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<GameResponse>> list() {
        var response = this.gameService.getAllGame().stream().map(this::mapGameToResponse).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GameResponse> get(
            @RequestParam("gameId") long gameId,
            @RequestParam("operationCount") long operationCount) {

        var response = this.mapGameToResponse(this.gameService.getGame(gameId, operationCount));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/selectMembers", method = RequestMethod.POST)
    public ResponseEntity<Object> selectMembers(@RequestBody SelectMembersRequest req) {
        this.gamePlayingService.selectMembers(req.getGameId(), req.getSelectedPlayerId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/vote", method = RequestMethod.POST)
    public ResponseEntity<Object> vote(@RequestBody VoteRequest req) {
        var vote = Vote.codeOf(req.getVote()).orElseThrow(() -> new IllegalArgumentException("illegal vote code"));
        this.gamePlayingService.vote(req.getGameId(), req.getPlayerId(), vote);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/mission", method = RequestMethod.POST)
    public ResponseEntity<Object> mission(@RequestBody MissionRequest req) {
        this.gamePlayingService.playMission(req.getGameId(), req.getIsSuccess() == 1);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/reset", method = RequestMethod.POST)
    public ResponseEntity<Object> reset(@RequestBody ResetRequest req) {
        this.gameService.resetGame(req.getGameId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GameをGameResponseにマッピングする。
     * 
     * @param game
     * @return GameResponse
     */
    private GameResponse mapGameToResponse(Game game) {
        var res = new GameResponse();
        BeanUtils.copyProperties(game, res);
        return res;
    }

}
