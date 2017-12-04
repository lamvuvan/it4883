/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.util;

import com.lamvu.http.HttpRequest;
import com.lamvu.http.HttpResponse;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author lamvu
 */
public class RequestHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class.getName());
    
    private Socket socket;
    
    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpRequest req = new HttpRequest(socket.getInputStream());
            HttpResponse resp = new HttpResponse(req);
            resp.write(socket.getOutputStream());
            socket.close();
        } catch (IOException ex) {
            LOGGER.error("Runtime Error ", ex);
        }
    }
}
