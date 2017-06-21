package com.talesdev.judge.program;

import java.util.List;
import java.util.Map;

/**
 * Default Program
 */
public final class DefaultProgram implements Program{

    @Override
    public ProgramOutput run(List<String> args, Map<String, String> options) {
        return new EmptyOutput();
    }

    @Override
    public boolean empty() {
        return true;
    }
}
