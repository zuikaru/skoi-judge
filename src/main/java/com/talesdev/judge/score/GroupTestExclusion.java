package com.talesdev.judge.score;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Group Test
 */
public class GroupTestExclusion {
    private final Set<Integer> groups;
    private final int total;

    public GroupTestExclusion(int total, Integer ...exclusions) {
        this.total = total;
        groups = Sets.newHashSet(exclusions);
    }

    /**
     * TODO: FIX ME
     * @return
     */
    public List<Integer> list(){
        return new ArrayList<>();
    }
}
