package com.talesdev.judge.task;

import com.talesdev.judge.ConfigMap;
import com.talesdev.judge.Key;
import com.talesdev.judge.Server;
import com.talesdev.judge.program.FileProgram;
import com.talesdev.judge.sandbox.IsolateSandbox;
import com.talesdev.judge.sandbox.SandboxOutput;
import com.talesdev.judge.score.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Batch type programming task
 */
public final class BatchTask {
    private final Server server;
    private final String codeName;
    private final long submissionTime;
    private final int timeLimit;
    private final int memoryLimit;
    private final List<String> testCases;
    private final List<String> solutions;

    public BatchTask(Server server, String codeName, long submissionTime, int timeLimit, int memoryLimit, List<String> testCases, List<String> solutions) {
        this.server = server;
        this.codeName = codeName;
        this.submissionTime = submissionTime;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.testCases = testCases;
        this.solutions = solutions;
    }

    /**
     * Synchronous method for running with each test case
     *
     * @param program program
     * @return score
     */
    public SumScore run(FileProgram program) throws Exception {
        int id = server.generateID();
        File location = program.programLocation();
        // Prepare input files
        List<File> testFiles = new ArrayList<>();
        int index = 1;
        for (String input : testCases) {
            File f = new File(server.config().getIsolateData() + "/" + id + "/" + index + ".in");
            testFiles.add(f);
            FileUtils.write(f, input.concat("\n"), "UTF-8");
            Set<PosixFilePermission> perms = new HashSet<>();
            //add owners permission
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(f.toPath(), perms);
            index++;
        }
        // initial options
        String memLimit = Integer.toString(memoryLimit);
        Map<String, String> options = new ConfigMap(
                new Key("cg", ""),
                new Key("mem", memLimit),
                new Key("cg-mem", memLimit),
                new Key("time", new DecimalFormat("0.000").format(timeLimit / 1000.D))
        ).map();
        // Prepare sandbox
        IsolateSandbox sandbox = new IsolateSandbox(server, id, location, new HashMap<>());
        // run each test case
        List<Verdict> verdicts = new ArrayList<>();
        int index2 = 0;
        for (File f : testFiles) {
            options.put("stdin", f.getName());
            SandboxOutput opt = sandbox.run(options, Collections.singletonList(f));
            Map<String, String> meta = opt.meta();
            int time = (int) (Double.parseDouble(meta.get("time"))*1000.D);
            int memory = Integer.parseInt(meta.get("cg-mem"));
            Verdict v;
            if (meta.containsKey("status")) {
                String status = meta.get("status");
                switch (status) {
                    case "RE":
                    case "SG":
                        v = new SingleVerdict(Reason.RUNTIME_ERROR,time,memory);
                        break;
                    case "TO":
                        v = new SingleVerdict(Reason.TIME_LIMIT,time,memory);
                        break;
                    case "XX":
                        v = new SingleVerdict(Reason.JUDGE_ERROR,time,memory);
                        break;
                    default:
                        v = new SingleVerdict(Reason.UNKNOWN,time,memory);
                        break;
                }
            } else {
                // begin comparison
                String output = opt.programOutput();
                String solution = solutions.get(index2);
                StringTokenizer outputToken = new StringTokenizer(output);
                StringTokenizer solutionToken = new StringTokenizer(solution);
                boolean equals = true;
                while (outputToken.hasMoreTokens()) {
                    if (solutionToken.hasMoreTokens()) {
                        if (!outputToken.nextToken().equals(solutionToken.nextToken())) {
                            equals = false;
                            break;
                        }
                    } else {
                        equals = false;
                        break;
                    }
                }
                if (solutionToken.hasMoreTokens())
                    equals = false;
                if (equals)
                    v = new SingleVerdict(Reason.ACCEPTED,time,memory);
                else
                    v = new SingleVerdict(Reason.WRONG_ANSWER,time,memory);
            }
            // verdict
            verdicts.add(v);
            index2++;
        }
        return new SumScore(verdicts);
    }

    public long submissionTime() {
        return submissionTime;
    }

    public int timeLimit() {
        return timeLimit;
    }

    public int memoryLimit() {
        return memoryLimit;
    }

    public String codeName() {
        return codeName;
    }
}
