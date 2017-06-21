package com.talesdev.judge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Compiler playground
 * Created by MoKunz on 11/6/2016.
 */
public class Test {
    public static void main(String[] args) {
        String std = "c++11";
        String extension = ".cpp";
        String programName = "fib";
        File location = new File(System.getProperty("user.home"),programName + extension);
        File outputLoc = new File(System.getProperty("user.home"),programName);
        ProcessBuilder pb = new ProcessBuilder("g++","-std=" + std,"-O2",location.getPath(),"-o",outputLoc.getAbsolutePath());
        StringBuilder compilerOutput = new StringBuilder();
        StringBuilder compilerError = new StringBuilder();
        try {
            Process p = pb.start();
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream())),
                    error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while((line = output.readLine()) != null)
                compilerOutput.append(line).append("\n");
            while((line = error.readLine()) != null)
                compilerError.append(line).append("\n");

        } catch (IOException e) {
            System.out.println("Error while starting compiler for \"" + programName + "\"");
            e.printStackTrace();
        }
        System.out.println("> Compilation done");
        System.out.println("  - Output created? " + (outputLoc.exists() ? "yes" : "no"));
        System.out.println("> Compiler messages:");
        System.out.println(compilerOutput.toString());
        System.out.println("> Compiler errors:");
        System.out.println(compilerError.toString());
    }
}
