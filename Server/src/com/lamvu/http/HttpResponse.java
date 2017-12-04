/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 *
 * @author lamvu
 */
public class HttpResponse {

    private static final Logger LOGGER = Logger.getLogger(HttpResponse.class.getName());

    public static final String VERSION = "HTTP/1.1";

    List<String> headers = new ArrayList<>();

    byte[] body;

    public HttpResponse(HttpRequest req) {
        switch (req.method) {
            case GET:
                try {
                    fillHeaders(Status._200);
                    File file = new File("." + req.uri);
                    if (file.isDirectory()) {
                        headers.add(ContentType.HTML.toString());
                        StringBuilder result = new StringBuilder("<html><head><title>");
                        result.append(req.uri);
                        result.append("</title></head><body><h1>Index of ");
                        result.append(req.uri);
                        result.append("</h1>");

                        File[] files = file.listFiles();
                        for (File subFile : files) {
                            result.append("<a href=\"" + subFile.getPath() + "\">" + subFile.getPath() + "<a/>\n");
                        }
                        result.append("</body></html>");
                        fillResponse(result.toString());
                    } else if (file.exists()) {
                        LOGGER.info("IP:" + String.format("%d.%d.%d.%d",
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255), new Random().nextInt(255)) + " " + req.method + " " + req.uri + " " + VERSION + " " + "Code:" + Status._200);
                        LOGGER.debug("IP:" + String.format("%d.%d.%d.%d",
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255), new Random().nextInt(255)) + " " + req.method + " " + req.uri + " " + VERSION + " " + "Code:" + Status._200);
                        setContentType(req.uri, headers);
                        fillResponse(getBytes(file));
                    } else {
                        LOGGER.error("IP:" + String.format("%d.%d.%d.%d",
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255), new Random().nextInt(255)) + " " + req.method + " " + req.uri + " " + VERSION + " " + "Code:" + Status._404);
                        fillHeaders(Status._404);
                        fillResponse(Status._404.toString());
                    }
                } catch (Exception e) {
                    LOGGER.error("IP:" + String.format("%d.%d.%d.%d",
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255), new Random().nextInt(255)) + " " + "Code:" + Status._400, e);
                    fillHeaders(Status._400);
                    fillResponse(Status._400.toString());
                }
                break;
            case UNRECOGNIZED:
                LOGGER.error("IP:" + String.format("%d.%d.%d.%d",
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255), new Random().nextInt(255)) + " " + "Code:" + Status._400);
                fillHeaders(Status._400);
                fillResponse(Status._400.toString());
                break;
            default:
                LOGGER.error("Code:" + Status._501);
                fillHeaders(Status._501);
                fillResponse(Status._501.toString());
        }
    }

    private byte[] getBytes(File file) throws FileNotFoundException, IOException {
        int length = (int) file.length();
        byte[] array = new byte[length];
        InputStream in = new FileInputStream(file);
        int offset = 0;
        while (offset < length) {
            int count = in.read(array, offset, (length - offset));
            offset += count;
        }
        in.close();
        return array;
    }

    public void write(OutputStream out) throws IOException {
        DataOutputStream output = new DataOutputStream(out);
        for (String header : headers) {
            output.writeBytes(header + "\r\n");
        }
        output.writeBytes("\r\n");
        if (body != null) {
            output.write(body);
        }
        output.writeBytes("\r\n");
        output.flush();
    }

    private void fillHeaders(Status status) {
        headers.add(VERSION + " " + status.toString());
        headers.add("Connection: closed");
        headers.add("Server: My Web Server");
    }

    private void fillResponse(String response) {
        body = response.getBytes();
    }

    private void fillResponse(byte[] response) {
        body = response;
    }

    private void setContentType(String uri, List<String> list) {
        try {
            String ext = uri.substring(uri.indexOf(".") + 1);
            list.add(ContentType.valueOf(ext.toUpperCase()).toString());
        } catch (Exception e) {
            LOGGER.error("ContentType not found: " + e, e);
        }
    }
    
}
