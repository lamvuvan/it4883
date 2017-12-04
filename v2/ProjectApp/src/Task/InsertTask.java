/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Config.AppConfig;
import Config.Hbase;
import Object.ListCommit;
import Object.Log;
import Object.QueueExct;
import Utils.ConfigurationUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 *
 * @author linhpn
 */
public class InsertTask extends Thread{
    private static Logger logger  = Logger.getLogger(InsertTask.class);
    private static HashMap<String, QueueExct> mapqueues;
    private static QueueExct queue;
    private ListCommit listCommit;
    private static HTable table;
    private Runnable commitAmount = new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    listCommit.commitAmount();
                }catch (IOException ex) {
                    java.util.logging.Logger.getLogger(InsertTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
    public InsertTask() throws IOException{
        table = ConfigurationUtils.getHTable(Hbase.TABLE_LOG);
        this.listCommit = new ListCommit(table);
    }
    
    public static void setMapQueues(HashMap<String,QueueExct> mapqueues){
        InsertTask.mapqueues = mapqueues;
    }
    public static void setQueue(QueueExct queue){
        InsertTask.queue = queue;
    }
    public static void setTable(HTable table){
        InsertTask.table = table;
    }
    @Override
    public void run() {
        Thread t = new Thread(commitAmount);
        t.start();
        while(true){
            try {
                process();
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(InsertTask.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(InsertTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public byte[] parseIP(String ip){
        byte[] key;
        Matcher m;
        m = Pattern.compile(AppConfig.IP_PATTERN).matcher(ip);
        if(m.matches()){
            key = Bytes.toBytes(Integer.parseInt(m.group(2)));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(3))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(4))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(5))));
            return key;
        }
        else{
//            logger.info(m.matches());
        }
        
        return null;
    }
    public void process() throws InterruptedException, IOException{
        Log msg = queue.dequeue();
//        logger.info(msg.getIpaddress().substring(msg.getIpaddress().length()-1));
        byte[] rowkey = Bytes.toBytes(msg.getIpaddress().substring(msg.getIpaddress().length()-1));
        rowkey = Bytes.add(rowkey,Bytes.toBytes(msg.getDate()));
        rowkey = Bytes.add(rowkey,parseIP(msg.getIpaddress()));
        Put p = new Put(rowkey);
        p.add(Bytes.toBytes(Hbase.COLFAM),Bytes.toBytes(Hbase.QUALIFITER_LOG), Bytes.toBytes(msg.getContent()));
        listCommit.add(p,msg.getStatus());
        listCommit.commit();
    }
}
