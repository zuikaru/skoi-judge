package com.talesdev.judge.sandbox;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.talesdev.judge.ConfigMap;
import com.talesdev.judge.Key;
import com.talesdev.judge.Server;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Isolate sandbox
 * Created by MoKunz on 1/29/2017.
 */
public final class IsolateSandbox {
    private final String ISOLATE;
    private final File DATA_DIR;
    private final String PASSWORD;
    private int counter = 0;
    private final int id;
    private final File program;
    private final Map<String, String> configMap;
    private boolean init = false;
    private final Server server;

    public IsolateSandbox(Server server, int id, File program, Map<String, String> config) {
        this.id = id;
        this.program = program;
        this.configMap = config;
        this.server = server;
        this.PASSWORD = server.config().getRootPassword();
        this.ISOLATE = server.config().getIsolateRoot() + "/isolate";
        this.DATA_DIR = new File(server.config().getIsolateData());
    }

    /**
     * Run the sandbox with given options
     *
     * @param options options for isolate sandbox, blank string for option without value
     * @param files   files to be copied
     * @return Sandbox running result
     */
    public SandboxOutput run(Map<String, String> options, List<File> files) throws Exception {
        if (!init) initialize();
        // increase counter
        counter++;
        StringBuilder cmds = new StringBuilder();
        // default config
        String metaFile = DATA_DIR.getAbsolutePath() + "/" + id + "/meta-" + counter + ".txt";
        Map<String, String> cfg = new ConfigMap(
                new Key("meta", metaFile),
                new Key("box-id", String.valueOf(id)),
                new Key("stdout", counter + ".out"),
                new Key("stderr", counter + ".err"),
                new Key("cg", "")
        ).map();
        options.putAll(cfg);
        // copy files
        for (File f : files)
            copyToBox(f);
        // create meta file
        File metaFileObject = new File(metaFile);
        metaFileObject.getParentFile().mkdirs();
        metaFileObject.createNewFile();
        // Options command
        options.entrySet().stream().filter(e -> !e.getKey().equals("")).forEach(e -> {
            cmds.append("--").append(e.getKey());
            if (e.getValue() != null && !e.getValue().equals(""))
                cmds.append("=").append(e.getValue());
            cmds.append(" ");
        });
        // Program command
        cmds.append("--").append("run ").append(program.getName());
        String isolateOutput;
        try {
            Process isolate = executeIsolateCommand(cmds.toString());
            if(isolate.waitFor() > 1)
                throw new Exception("Isolate process return non-zero exit code");
        } catch (IOException e) {
            throw new Exception("Isolate process execution failed", e);
        }
        // Copy result
        List<String> results = Lists.newArrayList(counter + ".out", counter + ".err");
        // create output directory
        new File(DATA_DIR.getAbsolutePath() + "/" + id + "/").mkdirs();
        // copy output from sandbox to user accessible output directory
        for (String s : results){
            if(executeRootCommand("cp " + boxDirectory() + s + " " +
                            DATA_DIR.getAbsolutePath() + "/" + id + "/")
                    .waitFor() != 0)
                throw new Exception("Output copy command return non-zero exit code");
        }
        // Read output
        String baseFolder = DATA_DIR.getAbsolutePath() + "/" + id + "/";
        String output = FileUtils.readFileToString(new File(baseFolder + options.get("stdout")), Charsets.UTF_8);
        String error = FileUtils.readFileToString(new File(baseFolder + options.get("stderr")), Charsets.UTF_8);
        List<String> rawMeta = FileUtils.readLines(new File(metaFile), Charsets.UTF_8);
        Map<String, String> meta = new HashMap<>();
        for(String metaEntry : rawMeta){
            String[] entry = metaEntry.split(":");
            meta.put(entry[0],entry[1]);
        }
        return new SandboxOutput(this, output, options, meta, error, output);
    }

    public Map<String, String> configMap() {
        return new HashMap<>(configMap);
    }

    public File program() {
        return program;
    }

    private void initialize() throws Exception {
        try {
            Process p = executeIsolateCommand("--cg --b=" + id + " --init");
            if (p.waitFor() != 0)
                throw new Exception("Isolate initialization does not return 0");
            // copy program
            copyToBox(program);
            init = true;
        } catch (IOException e) {
            System.out.println("Error while initializing sandbox");
            throw new Exception("Isolate process execution failed", e);
        }
    }

    public void cleanup() {
        try {
            Process p = executeIsolateCommand("--cleanup");
            p.waitFor();
            init = false;
        } catch (IOException e) {
            System.out.println("Error while cleaning up");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Failed to cleanup sandbox");
        }
    }

    private Process executeIsolateCommand(String args) throws IOException {
        return executeRootCommand(ISOLATE + " " + args);
    }

    private Process executeRootCommand(String cmd) throws IOException {
        String[] cmds = {"/bin/bash", "-c", "echo " + PASSWORD + "| sudo -S " + cmd};
        return Runtime.getRuntime().exec(cmds);
    }

    private void copyToBox(File file) throws IOException, InterruptedException {
        executeRootCommand("cp " + file.getPath() + " /tmp/box/" + id + "/box/").waitFor();
    }

    private String boxDirectory() {
        return "/tmp/box/" + id + "/box/";
    }
}
