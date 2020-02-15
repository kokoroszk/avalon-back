package com.kokoroszk.avalonback.request.game;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequest {

    private String gameName;

    private List<String> playerNames;

    private List<Integer> additionalRoles;
}
