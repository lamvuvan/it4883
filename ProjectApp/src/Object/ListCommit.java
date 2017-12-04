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
    private ArrayList<Put> listPut;
    private static ReentrantLock lock = new ReentrantLock();
    private HTable htable;
    private static HTable hTable_analyze;
    private static Logger logger = Logger.getLogger(ListCommit.class);
    private String topic;
    private volatile long amount=0;
    private long startTime = System.currentTimeMillis();
    private long startCommitAmount = System.currentTimeMillis();
    public ListCommit(HTable htable, String topic){
        this.htable = htable;
        this.listPut = new ArrayList<>();
        this.topic = topic;
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
    public void commit() throws InterruptedException, IOException{
        lock.lock();
        try{
            long time = System.currentTimeMillis();
            if((time-startTime)>=500&&listPut.size()>=AppConfig.COMMIT_MINIMUN_SIZE){
                amount+= listPut.size();
                htable.put(listPut);
                htable.flushCommits();
                listPut.clear();
                startTime = System.currentTimeMillis();
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
                hTable_analyze.incrementColumnValue(Bytes.toBytes("ERRORDISPLAY"), Bytes.toBytes("info"), Bytes.toBytes("Amount"), amount);
                hTable_analyze.flushCommits();
                startCommitAmount = System.currentTimeMillis();
                amount =0;
            }
        }finally{
            lock.unlock();
        }
    }
}
