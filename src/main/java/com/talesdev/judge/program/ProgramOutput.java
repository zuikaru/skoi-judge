package com.talesdev.judge.program;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Program output
 * TODO: Proper memory, time usage output
 * @author MoKunz
 */
public abstract class ProgramOutput {
    public abstract String string();
    public abstract String error();
}
