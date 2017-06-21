package com.talesdev.judge.lang;

import com.talesdev.judge.program.FileProgram;

/**
 * Compiler output
 * Created by pavat on 1/2/2017.
 */
public final class CompilerOutput {
    private final FileProgram program;
    private final String output;
    private final String error;
    private final int exitCode;

    public CompilerOutput(FileProgram program, String output, String error, int exitCode) {
        this.program = program;
        this.output = output;
        this.error = error;
        this.exitCode = 0;
    }

    public FileProgram program() {
        return program;
    }

    public String output() {
        return output;
    }

    public String error() {
        return error;
    }

    public int exitCode() {
        return exitCode;
    }
}
