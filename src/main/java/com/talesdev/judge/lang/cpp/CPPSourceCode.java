package com.talesdev.judge.lang.cpp;

import com.talesdev.judge.NoExtension;
import com.talesdev.judge.lang.CompilerOutput;
import com.talesdev.judge.lang.SourceCode;
import com.talesdev.judge.program.NativeProgram;
import com.talesdev.judge.program.Program;
import com.talesdev.judge.program.ProgramOutput;
import com.talesdev.judge.program.StreamOutputCollection;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * C++ SourceCode
 * Created by MoKunz on 10/30/2016.
 */
public final class CPPSourceCode implements SourceCode {

    private final File location;
    private final Logger log = Logger.getLogger("CPPCompiler");

    public CPPSourceCode(File location) {
        this.location = location;
    }

    @Override
    public List<String> sourceCode() throws IOException {
        return Files.readAllLines(location.toPath());
    }

    @Override
    public File location() {
        return location;
    }

    @Override
    public String language() {
        return "C++";
    }

    @Override
    public CompilerOutput compile(Map<String, String> args) throws Exception {
        String std = args.containsKey("std") ? args.get("std") : "c++11";
        String programName = new NoExtension(location.getName()).name();
        ProcessBuilder pb = new ProcessBuilder("g++","-std=" + std,"-O2",location.getPath(),"-o",location.getParent() + "/" + programName);
        // Start the compilation process
        String compilerMessage = "", compilerError = "";
        int exitCode;
        try {
            Process process = pb.start();
            exitCode = process.waitFor();
            compilerMessage = new StreamOutputCollection(process.getInputStream()).asString();
            compilerError = new StreamOutputCollection(process.getErrorStream()).asString();

        } catch (IOException e) {
            throw new Exception("Compilation failed ",e);
        }
        File programLocation = new File(location.getParent(),programName);
        return new CompilerOutput(new NativeProgram(programLocation),compilerMessage,compilerError,exitCode);
    }
}
