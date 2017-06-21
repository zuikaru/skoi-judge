package com.talesdev.judge.program;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Program
 * TODO: more abstraction layer
 * Created by MoKunz on 10/17/2016.
 */
public interface Program {
    ProgramOutput run(List<String> args, Map<String, String> options) throws IOException;
    boolean empty();
}
