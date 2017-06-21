package com.talesdev.judge.task;

import java.util.List;
import java.util.Map;

/**
 * Judge request
 */
public class JudgeRequest {
    private String user;
    private String apiToken;
    private Map<String, String> submission;
    private TaskModel taskModel;
    private TaskSupply taskSupply;

    public String getUser() {
        return user;
    }

    public String getApiToken() {
        return apiToken;
    }

    public Map<String, String> getSubmission() {
        return submission;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public TaskSupply getTaskSupply() {
        return taskSupply;
    }

    public static class TaskModel {
        private String name;
        private String codeName;
        private int timeLimit;
        private int memoryLimit;
        private String description;
        private String type;
        private List<Integer> groupRule;

        public String getName() {
            return name;
        }

        public String getCodeName() {
            return codeName;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public int getMemoryLimit() {
            return memoryLimit;
        }

        public String getDescription() {
            return description;
        }

        public String getType() {
            return type;
        }

        public List<Integer> getGroupRule() {
            return groupRule;
        }
    }
}
