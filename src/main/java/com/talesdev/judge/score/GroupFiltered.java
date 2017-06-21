package com.talesdev.judge.score;

import com.talesdev.judge.Sum;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class to do group test
 */
public class GroupFiltered implements Score{
    private final SumScore sumScore;
    private final List<Integer> groupRange;

    public GroupFiltered(SumScore sumScore, List<Integer> groupRange) {
        this.sumScore = sumScore;
        this.groupRange = groupRange;
    }

    private boolean validateRule(){
        // Check for empty group rule
        if(groupRange.size() <= 0)
            return false;
        // Check for invalid group rule
        int sum = 0;
        for(int i : groupRange) sum += i;
        return sum >= sumScore.total();
    }

    @Override
    public boolean accepted() {
        return passed() == sumScore.total();
    }

    @Override
    public Reason reason() {
        return sumScore.reason();
    }

    @Override
    public String code() {
        if(!validateRule()) return sumScore.code();
        // Begin group rule
        Iterator<Verdict> vi = sumScore.all().iterator();
        StringBuilder sb = new StringBuilder();
        for(int r : groupRange){
            if(r > 1) sb.append('[');
            for(int i = 0; i < r; i++){
                if(vi.hasNext())
                    sb.append(vi.next().code());
                else
                    break;
            }
            if(r > 1) sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int time() {
        return sumScore.time();
    }

    @Override
    public int memory() {
        return sumScore.memory();
    }

    @Override
    public List<Verdict> all() {
        return sumScore.all();
    }

    @Override
    public int passed(){
        if(!validateRule()) return sumScore.passed();
        int passed = 0;
        Iterator<Verdict> vi = sumScore.all().iterator();
        for(int r : groupRange){
            boolean p = true;
            for(int i = 0; i < r; i++){
                if(vi.hasNext())
                    p = p && vi.next().accepted();
                else
                    break;
            }
            if(p) passed++;
        }
        return passed;
    }

    @Override
    public int total() {
        return sumScore.total();
    }

    @Override
    public double percentage() {
        return passed()*100.D/total();
    }
}
