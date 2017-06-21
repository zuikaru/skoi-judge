package com.talesdev.judge.sandbox;

import java.util.Map;

/**
 * Sandbox output
 * Created by MoKunz on 1/29/2017.
 */
public class SandboxOutput {
    private final String rawOutput;
    private final IsolateSandbox sandbox;
    private final Map<String, String> options;
    private final Map<String, String> meta;
    private final String programOutput;
    private final String programError;

    public SandboxOutput(IsolateSandbox sandbox, String rawOutput, Map<String, String> options,
                         Map<String,String> meta, String programError, String programOutput) {
        this.sandbox = sandbox;
        this.rawOutput = rawOutput;
        this.options = options;
        this.meta = meta;
        this.programError = programError;
        this.programOutput = programOutput;
    }

    public String rawOutput(){
        return rawOutput;
    }

    public IsolateSandbox sandbox(){
        return sandbox;
    }

    public Map<String,String> options(){
        return options;
    }

    public Map<String,String> meta(){
        return meta;
    }

    public String programOutput() {
        return programOutput;
    }

    public String programError() {
        return programError;
    }
}
