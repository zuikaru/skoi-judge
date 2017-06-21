package com.talesdev.judge;

/**
 * Main Class
 *
 * @author MoKunz
 */
public class Main {
    /**
     * Start the judge server
     *
     * @param args startup arguments
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(args);
        server.run();
    }
}
