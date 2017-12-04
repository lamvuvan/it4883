/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Config.AppConfig;
import Object.Log;
import Object.QueueExct;
import Object.producerConfig;
import Utils.ParseUtils;
import com.sun.xml.internal.ws.developer.Serialization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import javax.sql.rowset.serial.SerialArray;
import org.apache.commons.lang.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author linhpn
 */
public class SendTask extends Thread {

    private static Logger logger = Logger.getLogger(SendTask.class);
    private static QueueExct queue;
    private KafkaProducer producer;
    private Properties props;
    private int id;
    private producerConfig proconfig;
    private long startTime = System.currentTimeMillis();
    private static volatile long EPS = 0;
    private ArrayList<File> files = new ArrayList<>();
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(AppConfig.DATE_FORMAT);
        }
    };
    private FilenameFilter filter;

    public void listingFile(String path, ArrayList<File> files) {
        File[] list = new File(path).listFiles(filter);
        if (list != null || list.length == 0) {
            for (File f : list) {
                if (f.isFile()) {
                    files.add(f);
                } else if (f.isDirectory()) {
                    listingFile(f.getAbsolutePath(), files);
                }
            }
        }
    }
//    private FilenameFilter filter = new FilenameFilter() {
//        @Override
//        public boolean accept(File dir, String name) {
//            return name.matches(AppConfig.FILE_NAME_PATTERN);
//        }
//    };
    private Runnable checkEPSTask = new Runnable() {
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            while (true) {
                long time = System.currentTimeMillis();
                if ((time - startTime) >= 1000) {
                    logger.info("Producer amount: " + EPS);
                    EPS = 0;
                    startTime = System.currentTimeMillis();
                }
            }
        }
    };

    public static void setQueue(QueueExct queue) {
        SendTask.queue = queue;
    }

    public SendTask(producerConfig producerConfig, FilenameFilter filter,int id) {
        this.proconfig = producerConfig;
        this.props = new Properties();
        this.filter = filter;
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, proconfig.getBoostrapServer());
        props.put(ProducerConfig.ACKS_CONFIG, proconfig.getAckConfig());
        props.put(ProducerConfig.LINGER_MS_CONFIG, proconfig.getLingermsConfig());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, proconfig.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, proconfig.getValueSerializer());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, proconfig.getBatchSizeConfig());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, proconfig.getBufferConfig());
        props.put(ProducerConfig.RETRIES_CONFIG, proconfig.getRetryConfig());
        this.producer = new KafkaProducer(props);
        this.id=id;
    }

    @Override
    public void run() {
//        if(id==0){
//        Thread t = new Thread(checkEPSTask);
//        t.start();
//        }
        while (true) {
            listingFile(AppConfig.PATH, files);

            for (File f : files) {
                try {
                    process(f);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(SendTask.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    java.util.logging.Logger.getLogger(SendTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (AppConfig.BACKUP) {
                    f.renameTo(new File(AppConfig.PATH_BACKUP + "/" + f.getName()));
                }
            }

            files.clear();

        }
    }

    public void process(File file) throws IOException, ParseException {
        FileInputStream fin = null;
        BufferedReader buf = null;
        try {
            fin = new FileInputStream(file);
            buf = new BufferedReader(new InputStreamReader(fin));
            String line = "";
            while ((line = buf.readLine()) != null) {
//                System.out.println(line);
//                Log msg = new Log();
//                msg.setDate(getTime(ParseUtils.parse("DATE", line)));
//                msg.setIpaddress(ParseUtils.parse("IP", line));
//                msg.setServiceType(ParseUtils.parse("SERVICE_TYPE", line));
//                msg.setContent(line);
//                if (msg.getDate() != 0 && !msg.getIpaddress().equals("") && !msg.getServiceType().equals("")) {
                    send(line);
//                    EPS++;
//                }
            }
            if(AppConfig.BACKUP){
                file.renameTo(new File(AppConfig.PATH_BACKUP+file.getName()));
            }else{
                file.delete();
            }
        } finally {
            buf.close();
            fin.close();
        }
    }

    public void send(String msg) {
        producer.send(new ProducerRecord("LOG", msg));
    }

    public long getTime(String source) throws ParseException {
        try {
            Date d = df.get().parse(source);
            return d.getTime();
        } catch (Exception e) {
            System.out.println("Send task: " + source);

        }
        return 0l;
    }
}
