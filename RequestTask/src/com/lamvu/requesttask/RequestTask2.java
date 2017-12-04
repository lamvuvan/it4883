/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.requesttask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lamvu
 */
public class RequestTask2 extends Thread {

    private static Logger LOGGER = Logger.getLogger(RequestTask2.class.getName());
    private static final String USER_AGENT = "Mozila/5.0";

    private Socket serverConn;
    private String name;

    private static long startTime = System.currentTimeMillis();
    private static volatile long EPS = 0;

    public RequestTask2(String host, int port, String name) {
        this.name = name;
        try {
            serverConn = new Socket(host, port);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public RequestTask2(String name) {
        this.name = name;
    }

    @Override
    public void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                while (true) {
                    long time = System.currentTimeMillis();
                    if ((time - startTime) >= 1000) {
                        LOGGER.info("EPS " + EPS);
                        EPS = 0;
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        });

        while (true) {
            sendGET("http://localhost:8080/profile.html");
        }
    }

    public static void main(String[] args) {
        RequestTask2 client = new RequestTask2("Thread 2");
        client.start();
        
    }

    private void sendGET(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = conn.getResponseCode();
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

}
