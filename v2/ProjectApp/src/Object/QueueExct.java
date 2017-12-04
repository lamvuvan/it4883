/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import Config.AppConfig;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author linhpn
 */
public class QueueExct {
    private Queue<Log> queue;
    private static ReentrantLock lock = new ReentrantLock();
    private static final Condition isNotEmpty = lock.newCondition();
    private static final Condition isNotFull = lock.newCondition();
    public QueueExct(){
        this.queue = new LinkedList<Log>();
    }
    public void enqueue(Log msg) throws InterruptedException{
        lock.lock();
        try{
            while(queue.size()>=AppConfig.LIMIT_QUEUE_SIZE){
                isNotFull.await();
            }
            queue.add(msg);
            isNotEmpty.signalAll();
        }finally{
            lock.unlock();
        }
    }
    public Log dequeue() throws InterruptedException{
        Log msg = null;
        try{
            lock.lock();
            while(queue.size()==0){
                isNotEmpty.await();
            }
            msg = queue.poll();
            isNotFull.signalAll();
            return msg;
        }finally{
            lock.unlock();
        }
        
    }
}
