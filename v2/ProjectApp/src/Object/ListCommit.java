/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import Config.AppConfig;
import Config.Hbase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 *
 * @author linhpn
 */
public class ListCommit {
    private static HashMap<String,List<Put>> mapPuts;
    private static HashMap<String,Integer> mapCommitAmounts;
    private ArrayList<Put> listPut;
    private static ReentrantLock lock = new ReentrantLock();
    private HTable htable;
    private static HTable hTable_analyze;
    private static Logger logger = Logger.getLogger(ListCommit.class);
    private volatile long amount=0;
    private long startTime = System.currentTimeMillis();
    private long startCommitAmount = System.currentTimeMillis();
    private boolean isCommit =false;
    public ListCommit(HTable htable){
        this.htable = htable;
        this.listPut = new ArrayList<>();
    }
    public static void setMapPuts(HashMap<String,List<Put>> mapPuts){
        ListCommit.mapPuts = mapPuts;
    }
    public static void setMapCommitAmounts(HashMap<String,Integer> mapCommitAmounts){
        ListCommit.mapCommitAmounts = mapCommitAmounts;
    }
    public static void setHTable(HTable htable){
        ListCommit.hTable_analyze = htable;
    }
    public void add(Put p) throws InterruptedException{
        lock.lock();
        try{
            listPut.add(p);
        }finally{
            lock.unlock();
        }
    }
    public void add(Put p,String topic){
        lock.lock();
        try{
            mapPuts.get(topic).add(p);
        }finally{
            lock.unlock();
        }
    }
    public void commit() throws InterruptedException, IOException{
        lock.lock();
        try{
            long time = System.currentTimeMillis();
            for(String topic: AppConfig.listTopic){
                if((((time-startTime)>=AppConfig.TIME_COMMIT_INTERVAL)||(mapPuts.get(topic).size()>=AppConfig.COMMIT_MINIMUN_SIZE))){
                    mapCommitAmounts.put(topic,mapCommitAmounts.get(topic)+mapPuts.get(topic).size());
                    htable.put(mapPuts.get(topic));
                    htable.flushCommits();
                    mapPuts.get(topic).clear();
                    isCommit = true;
                }
            }
            if(isCommit){
                startTime = System.currentTimeMillis();
                isCommit = false;
            }
        }finally{
            lock.unlock();
        }
    }
    public void commitAmount() throws IOException{
        lock.lock();
        try{
            long time = System.currentTimeMillis();
            if((time-startCommitAmount)>=1000){
                for(String topic: AppConfig.listTopic){
//                    if((time-startCommitAmount)>=1000){
//                        logger.info(topic);
//                        logger.info("Topic: "+topic+"EPS:"+mapCommitAmounts.get(topic));
                        hTable_analyze.incrementColumnValue(Bytes.toBytes(topic), Bytes.toBytes(Hbase.COLFAM), Bytes.toBytes(Hbase.QUALIFITER_AMOUNT), mapCommitAmounts.get(topic));
                        hTable_analyze.flushCommits();
                        mapCommitAmounts.put(topic,0);
//                    }
                }
                startCommitAmount = System.currentTimeMillis();
            }
            
            
        }finally{
            lock.unlock();
        }
    }
}
