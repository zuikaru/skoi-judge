package com.talesdev.judge.score;

/**
 * Verdict reason
 * Created by MoKunz on 10/16/2016.
 */
public enum Reason {
    ACCEPTED('P'),
    WRONG_ANSWER('-'),
    TIME_LIMIT('T'),
    RUNTIME_ERROR('X'),
    JUDGE_ERROR('*'),
    UNKNOWN('?');
    private char code;

    Reason(char code) {
        this.code = code;
    }

    public char code(){
        return code;
    }
}
