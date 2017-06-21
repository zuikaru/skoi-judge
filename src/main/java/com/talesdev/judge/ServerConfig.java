package com.talesdev.judge;

/**
 * Server config file
 */
public class ServerConfig {
    private String rootPassword = "moco2010\\$Z";
    private String isolateRoot = "%USER_HOME%/isolate-bin";
    private String isolateData = "%USER_HOME%/isolate-bin/data";
    private String queueHost = "localhost";
    private String queueUsername = "test";
    private String queuePassword = "test";
    private String queueVirtualHost = "/";
    private String queueName = "judge";
    /**
     * API Main Entry point
     */
    private String notifyURL = "localhost/api/judge";

    public String getRootPassword() {
        return rootPassword;
    }

    public String getIsolateRoot() {
        return processTemplate(isolateRoot);
    }

    public String getIsolateData() {
        return processTemplate(isolateData);
    }

    private String processTemplate(String proc) {
        return proc.replace("%USER_HOME%", Server.USER_HOME);
    }

    public String getQueueHost() {
        return queueHost;
    }

    public String getQueueUsername() {
        return queueUsername;
    }

    public String getQueuePassword() {
        return queuePassword;
    }

    public String getQueueVirtualHost() {
        return queueVirtualHost;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getNotifyURL() {
        return notifyURL;
    }
}
