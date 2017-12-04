/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import Utils.LogDeserializer;
import Utils.LogSerializer;

/**
 *
 * @author linhpn
 */
public class Kafka {
    public static final String BOOTSTRAP_SERVER_CONFIG="master:9092,slave1:9092,slave2:9092,slave3:9092";
    public static final String KEY_SERIALIZER_CONFIG = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String ACK_CONFIG ="1";
    public static final String VALUE_SERIALIZER_CONFIG = "org.apache.kafka.common.serialization.StringSerializer";
//    public static final String VALUE_SERIALIZER_CONFIG = LogSerializer.class.getName();
    public static final int RETRY_CONFIG=0;
    public static final int BATCH_SIZE_CONFIG=16384;
    public static final int BUFFER_MEMORY_CONFIG=33554432;
    public static final int LINGER_MS_CONFIG =1;
    public static final boolean AUTO_COMMIT_CONFIG = true;
    public static final int COMMIT_INTERVAL_CONFIG = 1000;
    public static final int SESSION_TIMEOUT_CONFIG = 40000;
    public static final String KEY_DESERIALIZER_CONFIG = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String VALUE_DESERIALIZER_CONFIG = LogDeserializer.class.getName();
}
