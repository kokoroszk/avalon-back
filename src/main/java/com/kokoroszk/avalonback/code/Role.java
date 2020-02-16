package com.kokoroszk.avalonback.code;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ArthurianKnight(0, true, false),
    MinionOfMordred(1, false, false),
    Merlin(2, true, true),
    Mordred(3, false, true);

    private int code;

    private boolean isResistance;

    private boolean isAdditional;

    /** コードからEnumに変換するためのMap */
    private static final Map<Integer, Role> codeToEnum = Stream.of(Role.values())
            .collect(Collectors.toMap(Role::getCode, Function.identity()));

    private Role(int code, boolean isGoodSide, boolean isAdditional) {
        this.code = code;
        this.isResistance = isGoodSide;
        this.isAdditional = isAdditional;
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
