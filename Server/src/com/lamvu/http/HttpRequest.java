/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author lamvu
 */
public class HttpRequest {

    private static final Logger LOGGER = Logger.getLogger(HttpRequest.class);

    List<String> headers = new ArrayList<>();

    Method method;
    String uri;
    String version;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String str = reader.readLine();
        parseRequestLine(str);
        
        while(!str.equals("")){
            str = reader.readLine();
            parseRequestHeader(str);
        }
    }

    private void parseRequestLine(String str) {
        String[] split = str.split("\\s+");
        try {
            method = Method.valueOf(split[0]);
        }catch(Exception e){
            method = Method.UNRECOGNIZED;
        }
        uri = split[1];
        version = split[2];
    }
    
    private void parseRequestHeader(String str){
        headers.add(str);
    }
}
