package com.kokoroszk.avalonback.code;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 投票の状態を示す。
 */
public enum Vote {

    None(0),

    Accept(1),

    Reject(2);

    private int code;

    private static final Map<Integer, Vote> codeToEnum =
            Stream.of(Vote.values()).collect(Collectors.toMap(Vote::getCode, Function.identity()));

    private Vote(int code) {
        this.code = code;
    }

    @JsonValue
    public Integer getCode() {
        return this.code;
    }

    public static Optional<Vote> codeOf(int code) {
        return Optional.of(codeToEnum.get(code));
    }
}
