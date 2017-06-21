package com.talesdev.judge;

import java.util.function.Function;

/**
 * Sum
 * Created by MoKunz on 10/14/2016.
 */
public final class Sum{
    private final Function<Integer,Integer> numberFunction;
    private final int from;
    private final int to;

    public Sum(Function<Integer, Integer> numberFunction, int from, int to) {
        this.numberFunction = numberFunction;
        this.from = from;
        this.to = to;
    }

    public Sum(Function<Integer, Integer> numberFunction, int to){
        this(numberFunction,1,to);
    }

    public int value(){
        int value = 0;
        for(int i = from; i <= to; i++){
            value += numberFunction.apply(i);
        }
        return value;
    }

    public int from(){
        return from;
    }

    public int to(){
        return to;
    }

    public int size(){
        return to - from + 1;
    }
}
