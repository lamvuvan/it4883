/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Config.AppConfig;
import Object.Log;
import Task.SendTask;
import Utils.ParseUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author linhpn
 */
public class Test extends Thread{
    private ArrayList<File> files = new ArrayList<>();
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(AppConfig.DATE_FORMAT);
        }
    };
    public void listingFile(String path, ArrayList<File> files){
        File[] list = new File(path).listFiles();
        if(list!=null||list.length==0){
            for(File f: list){
                if(f.isFile()){
                    files.add(f);
                }
                else if(f.isDirectory()){
                    listingFile(f.getAbsolutePath(), files);
                }
            }
        }
    }
    public void process(File file) throws IOException, ParseException{
        FileInputStream fin = null;
        BufferedReader buf = null;
        try{
            fin = new FileInputStream(file);
            buf = new BufferedReader(new InputStreamReader(fin));
            String line = "";
            while((line=buf.readLine())!=null){
                Log msg = new Log();
                msg.setDate(getTime(ParseUtils.parse("DATE", line)));
                msg.setIpaddress(ParseUtils.parse("IP", line));
                msg.setServiceType(ParseUtils.parse("SERVICE_TYPE", line));
                msg.setContent(line);
//                msg.setHost("localhost");
//                msg.setPort("8080");
                if(line.contains(AppConfig.INFO)){
                    msg.setStatus(AppConfig.INFO);
                }
                else if(line.contains(AppConfig.WARNING)){
                    msg.setStatus(AppConfig.WARNING);
                }
                else if(line.contains(AppConfig.ERROR)){
                    msg.setStatus(AppConfig.ERROR);
                }
                System.out.println(msg.getDate());
//                send(msg);
            }
        }finally{
            buf.close();
            fin.close();
        }
    }
    public long getTime(String source) throws ParseException{
        Date d = df.get().parse(source);
        return d.getTime();
    }
    @Override
    public void run() {
        while(true){
            listingFile(AppConfig.PATH, files);
            for(File f: files){
                try {
                    process(f);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(SendTask.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    java.util.logging.Logger.getLogger(SendTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.delete();
            }
            files.clear();
        }
    }
    public static void main(String[] args) {
        String test ="abac";
        System.out.println(test.substring(test.length()-1));
    }
    
}
