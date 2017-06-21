package com.talesdev.judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import com.talesdev.judge.lang.CompilerOutput;
import com.talesdev.judge.lang.cpp.CPPSourceCode;
import com.talesdev.judge.program.FileProgram;
import com.talesdev.judge.score.GroupFiltered;
import com.talesdev.judge.score.SumScore;
import com.talesdev.judge.task.BatchTask;
import com.talesdev.judge.task.JudgeRequest;
import com.talesdev.judge.task.JudgeResponse;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Task Consumer
 */
public class TaskConsumer {
    private final Server server;
    private final Gson gson;
    private final Logger logger = Logger.getLogger("TaskConsumer");

    public TaskConsumer(Server server) {
        this.server = server;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void run() throws Exception {
        Channel channel = server.connection().createChannel();
        channel.queueDeclare(server.config().getQueueName(), true, false, false, null);
        channel.basicConsume(server.config().getQueueName(), true, consumer(channel));
    }

    private Consumer consumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String str = new String(body, "UTF-8");
                JudgeRequest request = gson.fromJson(str, JudgeRequest.class);
                // Grading task
                logger.log(Level.INFO, "Preparing task");
                // Prepare
                /*long createdAt = 0;
                try {
                    createdAt = DateFormat.getDateTimeInstance().parse(request.getSubmission().get("created_at")).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                // TODO: Parse datetime
                BatchTask task = new BatchTask(server,
                        request.getTaskModel().getCodeName(),
                        System.currentTimeMillis(),
                        request.getTaskModel().getTimeLimit(),
                        request.getTaskModel().getMemoryLimit(),
                        request.getTaskSupply().getInput(),
                        request.getTaskSupply().getSolution()
                );
                int id = Integer.parseInt(request.getSubmission().get("id"));
                List<Integer> group = request.getTaskModel().getGroupRule();
                // Notify in-progress to main server
                new JudgeResponse(server, request.getUser(), id, request.getApiToken(), "!IP").send();
                // Compile
                logger.log(Level.INFO, "Begin compilation");
                FileProgram program;
                String outputMsg, errorMsg = "";
                File programLocation = server.programLocation(
                        id,
                        request.getTaskModel().getCodeName(),
                        request.getSubmission().get("language")
                );
                try {
                    FileUtils.write(programLocation, request.getSubmission().get("source_code"), "UTF-8");
                    CompilerOutput output = new CPPSourceCode(programLocation).compile(new HashMap<>());
                    program = output.program();
                    outputMsg = output.output();
                    errorMsg = output.error();
                    if(output.exitCode() != 0 || !program.programLocation().exists())
                        throw new Exception("Compiler error: " + errorMsg);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Problem while compiling program");
                    logger.log(Level.WARNING, "Grading of this task will be canceled");
                    e.printStackTrace();
                    // Notify error to main server
                    new JudgeResponse(server, request.getUser(), id, request.getApiToken(), "!CE", e.toString(),errorMsg).send();
                    return;
                }
                logger.log(Level.INFO, "Compilation done");
                logger.log(Level.INFO, "Compiler output: " + outputMsg);
                logger.log(Level.INFO, "Compiler error: " + errorMsg);
                // run
                SumScore score = null;
                try {
                    logger.log(Level.INFO, "Start running program");
                    score = task.run(program);
                    logger.log(Level.INFO, "Finished");
                } catch (Exception e) {
                    logger.warning("Unable to run program, this will be reported as judge error");
                    e.printStackTrace();
                    new JudgeResponse(server, request.getUser(), id, request.getApiToken(), "!JE", e.toString(),errorMsg).send();
                    return;
                }
                logger.info("Calculating score");
                GroupFiltered groupScore = new GroupFiltered(score, group);
                logger.info("Calculation done, Submitting result to main server");
                System.out.println("Score: " + (int) groupScore.percentage());
                System.out.println("Result: " + groupScore.code());
                System.out.println("Time: " + groupScore.time());
                System.out.println("Memory: " + groupScore.memory());
                new JudgeResponse(server,
                        request.getUser(), id,
                        request.getApiToken(), "", "",
                        errorMsg,
                        groupScore.code(),
                        groupScore.passed(),
                        groupScore.total(),
                        groupScore.time(),
                        groupScore.memory()
                ).send();
                logger.info("Result submitted to main server");
            }
        };
    }
}
