package com.kokoroszk.avalonback.request.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequest {

    private long gameId;

    private int playerId;

    private int vote;

}
