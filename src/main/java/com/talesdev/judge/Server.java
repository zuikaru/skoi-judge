package com.talesdev.judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server
 */
public class Server {
    public static final String USER_HOME = System.getProperty("user.home");
    private final AtomicInteger id;
    private final File configFile;
    private ServerConfig config;
    private final Logger logger = Logger.getLogger("Server");
    private long startupTime = 0;
    private TaskConsumer taskConsumer;
    private Connection connection;
    private final String[] args;

    public Server(String[] args) {
        this.configFile = new File(USER_HOME, "skoi-judge-config.json");
        this.taskConsumer = new TaskConsumer(this);
        this.args = args;
        this.id = new AtomicInteger(args.length > 0 ? Integer.parseInt(args[0]) : 0);
    }

    public void run() throws Exception {
        logger.log(Level.INFO,"Initializing server (Atomic ID gen: " + id.get() + ")");
        startupTime = System.currentTimeMillis();
        // Check config file
        if (!configFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Can't create config file, fallback to default" + e);
            }
        }
        // read config file
        Gson gson = new GsonBuilder().create();
        logger.log(Level.INFO,"Reading server config file");
        config = gson.fromJson(gson.newJsonReader(new FileReader(configFile)), ServerConfig.class);
        if(config == null){
            config = new ServerConfig();
            logger.log(Level.WARNING,"Invalid or missing config file");
        }
        // construct connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getQueueHost());
        factory.setUsername(config.getQueueUsername());
        factory.setPassword(config.getQueuePassword());
        factory.setVirtualHost(config.getQueueVirtualHost());
        this.connection = factory.newConnection();
        logger.log(Level.INFO, "Connection to queue broker created!");
        // start task consumer
        this.taskConsumer.run();
        logger.log(Level.INFO, "Done!(" +
                new DecimalFormat("0.000").format((System.currentTimeMillis() - startupTime)/1000.D) +
                "s)"
        );
    }

    public int generateID() {
        return id.getAndIncrement()%100;
    }

    public ServerConfig config() {
        return config;
    }

    public Connection connection() {
        return connection;
    }

    public File programLocation(int id,String codeName,String extension){
        return new File(USER_HOME + "/tasks/" + codeName + "/" + codeName + "_" + id + "." + extension);
    }
}
