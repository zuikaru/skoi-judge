package com.talesdev.judge.lang;

import com.talesdev.judge.program.Program;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SourceCode
 * Created by MoKunz on 10/17/2016.
 */
public interface SourceCode{
    List<String> sourceCode() throws IOException;
    File location();
    String language();
    CompilerOutput compile(Map<String, String> args) throws Exception;
}
