package com.talesdev.judge.program;

import java.io.*;

/**
 * Collecting output from InputStream of Process
 * *note: methods are blocking
 * Created by MoKunz on 1/2/2017.
 */
public final class StreamOutputCollection {
    private final InputStream stream;

    public StreamOutputCollection(InputStream stream) {
        this.stream = stream;
    }

    public String asString() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null)
            output.append(line).append("\n");
        return output.toString();
    }
}
