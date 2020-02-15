package com.kokoroszk.avalonback.code;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ミッションの結果を示す。
 */
public enum Mission {

	AurthurWin(-1),
	MordretWin(-2),
	RequireTwo(2),
	RequireThree(3),
	RequireFour(4),
	RequireFive(5);
	
	private int code;

	private Mission(int code) {
		this.code = code;
	}

	@JsonValue
	public int getCode() {
	    return this.code;
	}
}
