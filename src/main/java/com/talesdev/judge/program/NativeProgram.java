package com.talesdev.judge.program;

import com.talesdev.judge.sandbox.IsolateSandbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Native program
 * TODO: Sandbox
 * Created by MoKunz on 1/2/2017.
 */
public class NativeProgram implements FileProgram {
    private final File location;

    public NativeProgram(File location) {
        this.location = location;
    }

    /**
     * Run program and get output/error (No sandbox/security)
     * @param args program args
     * @param options running options
     * @return ProgramOutput
     * @throws IOException
     */
    @Override
    public ProgramOutput run(List<String> args, Map<String, String> options) throws IOException {
        List<String> commands = new ArrayList<>();
        commands.add(location.getAbsolutePath());
        commands.addAll(args);
        Process process = new ProcessBuilder(commands).start();
        String output = new StreamOutputCollection(process.getInputStream()).asString(),
                error = new StreamOutputCollection(process.getErrorStream()).asString();
        return new ProgramOutput() {
            @Override
            public String string() {
                return output;
            }

            @Override
            public String error() {
                return error;
            }
        };
    }

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public File programLocation() {
        return location;
    }
}
