/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.server;

import com.lamvu.util.RequestHandler;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 *
 * @author lamvu
 */
public class Server extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static final int DEFAULT_PORT = 8080;

    private static final int N_THREADS = 6;

    public static void main(String[] args) {
        try {
            new Server().start(getValidPortParam(args));
        } catch (IOException ex) {
            LOGGER.error("Start Server Error", ex);
        }
    }

    public void start(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Listening on port " + port);
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        while (true) {
            Socket clientSocket = socket.accept();
            executorService.submit(new RequestHandler(clientSocket));
            InetAddress client = clientSocket.getInetAddress();
        }
    }

    static int getValidPortParam(String[] args) {
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            if (port > 0 && port < 65535) {
                return port;
            } else {
                throw new NumberFormatException("Invalid port! Range 0 to 65535");
            }
        }
        return DEFAULT_PORT;
    }
}
