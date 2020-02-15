package com.kokoroszk.avalonback.request.game;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectMembersRequest {

    private long gameId;

    private Set<Integer> selectedPlayerId;

}
