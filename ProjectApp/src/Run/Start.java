/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Run;

import Config.AppConfig;
import Config.Hbase;
import Config.Kafka;
import Object.ListCommit;
import Object.QueueExct;
import Object.consumerConfig;
import Object.producerConfig;
import Task.InsertTask;
import Task.ReceiveTask;
import Task.SendTask;
import Utils.ConfigurationUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.hadoop.hbase.client.HTable;

/**
 *
 * @author linhpn
 */
public class Start {
    private producerConfig proConfig;
    private consumerConfig conConfig;
    private ArrayList<SendTask> sendTasks = new ArrayList<>();
    private ArrayList<ReceiveTask> receiveTasks = new ArrayList<>();
    private ArrayList<InsertTask> insertTasks = new ArrayList<>();
    private HashMap<String, QueueExct> mapqueues = new HashMap<String, QueueExct>();
    private QueueExct queue = new QueueExct();
    private HTable table_log, table_analyze;
    private List<FilenameFilter> filters = new ArrayList<>();
    public Start() throws IOException{
        initialization();
    }
    public void initialization() throws IOException{
        FilenameFilter filter1 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AppConfig.FILE_NAME_PATTERN);
            }
        };
        FilenameFilter filter2 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AppConfig.FILE_NAME_PATTERN2);
            }
        };
        FilenameFilter filter3 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AppConfig.FILE_NAME_PATTERN3);
            }
        };
        FilenameFilter filter4 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AppConfig.FILE_NAME_PATTERN4);
            }
        };
        FilenameFilter filter5 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AppConfig.FILE_NAME_PATTERN5);
            }
        };
        filters.add(filter1);
        filters.add(filter2);
        filters.add(filter3);
//        filters.add(filter4);
//        filters.add(filter5);

//        this.table_log = ConfigurationUtils.getHTable(Hbase.TABLE_LOG);
//        this.table_analyze = ConfigurationUtils.getHTable(Hbase.TABLE_LOG_ANALYZE);
//        ListCommit.setHTable(this.table_analyze);
//        InsertTask.setTable(this.table_log);
        SendTask.setQueue(queue);
//        ReceiveTask.setTable(table_analyze);
        this.proConfig = new producerConfig();
//        this.conConfig = new consumerConfig();
        proConfig.setBoostrapServer(Kafka.BOOTSTRAP_SERVER_CONFIG);
        proConfig.setAckConfig(Kafka.ACK_CONFIG);
        proConfig.setBatchSizeConfig(Kafka.BATCH_SIZE_CONFIG);
        proConfig.setBufferConfig(Kafka.BUFFER_MEMORY_CONFIG);
        proConfig.setKeySerializer(Kafka.KEY_SERIALIZER_CONFIG);
        proConfig.setValueSerializer(Kafka.VALUE_SERIALIZER_CONFIG);
        proConfig.setLingermsConfig(Kafka.LINGER_MS_CONFIG);
        proConfig.setRetryConfig(Kafka.RETRY_CONFIG);
//        conConfig.setBoostrapServer(Kafka.BOOTSTRAP_SERVER_CONFIG);
//        conConfig.setAutoCommit(Kafka.AUTO_COMMIT_CONFIG);
//        conConfig.setCommitInterval(Kafka.COMMIT_INTERVAL_CONFIG);
//        conConfig.setKeyDeserializer(Kafka.KEY_DESERIALIZER_CONFIG);
//        conConfig.setValueDeserializer(Kafka.VALUE_DESERIALIZER_CONFIG);
//        conConfig.setSessionTimeout(Kafka.SESSION_TIMEOUT_CONFIG);
        for(int i=0;i<AppConfig.topics.length;i++){
            mapqueues.put(AppConfig.topics[i], new QueueExct());
        }
//        ReceiveTask.setMapQueues(mapqueues);
//        InsertTask.setMapQueues(mapqueues);
        initThread();
    }
    public void initThread() throws IOException{
        for(int i=0;i<filters.size();i++){
            SendTask task = new SendTask(proConfig,filters.get(i),i);
            sendTasks.add(task);
        }
//        for(int i=0;i<2;i++){
//            ReceiveTask task1 = new ReceiveTask("INFOPUT", conConfig,String.valueOf(i));
//            receiveTasks.add(task1);
//        }
//        for(int i=0;i<1;i++){
//            ReceiveTask task1 = new ReceiveTask("ERRORDISPLAY", conConfig,"3");
//            receiveTasks.add(task1);
//        }
//        for(int i=0;i<AppConfig.INSERTTASK_SIZE;i++){
//            InsertTask task = new InsertTask("INFOPUT");
//            insertTasks.add(task);
//        }    
    }
    public void start(){
//        for(InsertTask task: insertTasks){
//            task.start();
//        }
//        for(ReceiveTask task: receiveTasks){
//            task.start();
//        }
        for(SendTask task: sendTasks){
            task.start();
        }
    }
    public void stop(){
        for(SendTask task: sendTasks){
            task.stop();
        }
//        for(ReceiveTask task: receiveTasks){
//            task.stop();
//        }
//        for(InsertTask task: insertTasks){
//            task.stop();
//        }
    }
    public static void main(String[] args) throws IOException {
        Start start = new Start();
        start.start();
    }
}
