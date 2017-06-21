package com.talesdev.judge.score;

import com.talesdev.judge.Sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Sum score
 * @author MoKunz
 */
public final class SumScore implements Score{
    private final List<Verdict> verdicts;

    public SumScore(List<Verdict> verdicts) {
        this.verdicts = verdicts;
    }

    public SumScore(Verdict... verdicts){
        this(Arrays.asList(verdicts));
    }

    @Override
    public boolean accepted() {
        boolean accepted = true;
        for(Verdict v : verdicts){
            accepted = accepted && v.accepted();
        }
        return accepted;
    }

    @Override
    public Reason reason() {
        if(accepted())
            return Reason.ACCEPTED;
        else{
            return Reason.WRONG_ANSWER;
        }
    }

    @Override
    public String code() {
        StringBuilder b = new StringBuilder();
        verdicts.forEach(v -> b.append(v.code()));
        return b.toString();
    }

    @Override
    public int time() {
        return Collections.max(verdicts,(o1, o2) -> o1.time() - o2.time()).time();
    }

    @Override
    public int memory() {
        return Collections.max(verdicts,(o1, o2) -> o1.memory() - o2.memory()).memory();
    }

    @Override
    public List<Verdict> all() {
        return new ArrayList<>(verdicts);
    }

    @Override
    public int passed() {
        return new Sum((i) -> verdicts.get(i).accepted() ? 1 : 0, 0, verdicts.size() - 1).value();
    }

    @Override
    public int total() {
        return verdicts.size();
    }

    @Override
    public double percentage() {
        return passed()*100.D/total();
    }

    @Override
    public String toString() {
        return code();
    }
}
