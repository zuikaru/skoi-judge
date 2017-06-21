package com.talesdev.judge.task;

import java.util.List;
import java.util.Map;

/**
 * Task Supply
 */
public class TaskSupply {
    private List<String> input;
    private List<String> solution;
    private Map<String,String> validator;

    public List<String> getInput() {
        return input;
    }

    public List<String> getSolution() {
        return solution;
    }

    public String getSourceCodeValidator(){
        return validator.get("sourceCode");
    }

    public String getOutputValidator(){
        return validator.get("output");
    }
}
