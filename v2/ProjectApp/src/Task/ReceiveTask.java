/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Config.AppConfig;
import Config.Hbase;
import Config.Kafka;
import Object.Log;
import Object.QueueExct;
import Object.consumerConfig;
import Utils.ParseUtils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

/**
 *
 * @author linhpn
 */
public class ReceiveTask extends Thread{
    private static Logger logger = Logger.getLogger(ReceiveTask.class);
    private static HashMap<String, QueueExct> mapqueues;
    private static QueueExct queue;
    private String topic;
    private KafkaConsumer consumer;
    private Properties props;
    private String id;
    private consumerConfig consumConfig;
    private volatile long EPS=0;
    private static long startTime = System.currentTimeMillis();
    private static HTable table_analyze;
    public static void setTable(HTable table_analyze){
        ReceiveTask.table_analyze = table_analyze;
    }
    public static void setQueue(QueueExct queue){
        ReceiveTask.queue = queue;
    }
    public static void setMapQueues(HashMap<String, QueueExct> mapqueues){
        ReceiveTask.mapqueues = mapqueues;
    }
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(AppConfig.DATE_FORMAT);
        }
    };
//    private Runnable checkEPSTask = new Runnable() {
//        @Override
//        public void run() {
//            startTime = System.currentTimeMillis();
//            while (true) {
//                long time = System.currentTimeMillis();
//                if ((time - startTime) >= 1000) {
//                    writeEPS();
//                }
//            }
//        }
//    };
    public void writeEPS(){
        long time = System.currentTimeMillis();
        if ((time - startTime) >= 1000) {
            logger.info("EPS: "+EPS);
            if(EPS!=0){
//                logger.info("EPS: "+EPS);
                Put p = new Put(Bytes.toBytes(AppConfig.SERVER_CONFIG));
                p.add(Bytes.toBytes(Hbase.COLFAM), Bytes.toBytes(Hbase.QUALIFITER_EPS), Bytes.toBytes(EPS));
                try {
                    table_analyze.put(p);
                    table_analyze.flushCommits();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ReceiveTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                EPS = 0;
                startTime = System.currentTimeMillis();
            }
            
        }
    }
    public ReceiveTask(String topic, consumerConfig consumeConfig, String id){
        this.topic = topic;
        this.consumConfig = consumeConfig;
        this.props = new Properties();
        this.id = id;
        props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumeConfig.getBoostrapServer());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumConfig.isAutoCommit());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumConfig.getCommitInterval());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumConfig.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumConfig.getValueDeserializer());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumConfig.getSessionTimeout());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.topic);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, this.id);
        this.consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(Kafka.TOPIC));
    }
    @Override
    public void run() {
//        logger.info("Start to receive Message...");[
//        if(this.topic.equals("INFODISPLAY")){
//            Thread receiveTask = new Thread(checkEPSTask);
//            receiveTask.start();
//        }
        try {
            receive();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ReceiveTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ReceiveTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void receive() throws InterruptedException, ParseException{
        if(this.topic.equals(AppConfig.GROUP_PUT)){
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    Log msg = new Log();
                    msg.setDate(getTime(ParseUtils.parse("DATE", record.value())));
                    msg.setIpaddress(ParseUtils.parse("IP", record.value()));
                    msg.setStatus(ParseUtils.parse("STATUS", record.value()));
                    msg.setContent(record.value());
                    if(msg.getDate()!=0&&!msg.getIpaddress().equals("")&&(msg.getStatus()!=null)){
                        queue.enqueue(msg);
                    }                   
                }
            }
        }
        else{
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    EPS++;
                    writeEPS();
                }
            }
        }
    }
    public long getTime(String source) throws ParseException{
        Date d = df.get().parse(source);
        return d.getTime();
    }
}
