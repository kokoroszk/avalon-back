package com.kokoroszk.avalonback.code;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ArthurianKnight(0, true),
    MinionOfMordred(1, false),
    Merlin(2, true),
    Mordred(3, false);

    private int code;

    private boolean isResistance;

    private boolean isAdditional;

    /** コードからEnumに変換するためのMap */
    private static final Map<Integer, Role> codeToEnum = Stream.of(Role.values())
            .collect(Collectors.toMap(Role::getCode, Function.identity()));

    private Role(int code, boolean isGoodSide) {
        this.code = code;
        this.isResistance = isGoodSide;
    }

    @JsonValue
    public int getCode() {
        return this.code;
    }

    public boolean isResistance() {
        return this.isResistance;
    }

    public boolean isAdditional() {
        return this.isAdditional;
    }

    public static Optional<Role> codeOf(int code) {
        return Optional.of(codeToEnum.get(code));
    }
}
