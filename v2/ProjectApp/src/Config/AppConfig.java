/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author linhpn
 */
public class AppConfig {
    public static final String FILE_NAME_PATTERN ="log.txt.(.)*";
    public static final String IP_PATTERN ="((\\d+).(\\d+).(\\d+).(\\d+))";
    public static final String PARSE_IP="IP:((\\d+).(\\d+).(\\d+).(\\d+))";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN="((19|20)\\d\\d-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))";
    public static final String SERVICE_PATTERN="Code:(\\d+)";
    public static final String STATUS_PATTERN="(INFO|DEBUG|ERROR)";
    public static final String INFO = "INFO";
    public static final String WARNING = "DEBUG";
    public static final String ERROR = "ERROR";
    public static final long LIMIT_QUEUE_SIZE=200000;
    public static final String SERVER_CONFIG="SERVER1";
    public static final long COMMIT_MINIMUN_SIZE=15000;
    public static final long TIMEOUT_COMMIT=20000;
    public static final long TIME_COMMIT_INTERVAL=500;
    public static final String topics = "LOG";
    public static final String GROUP_PUT = "LOG1";
    public static final String GROUP_DISPLAY = "LOG2";
    public static final int SENDTASKS_SIZE=1;
    public static final int RECEIVETASK_SIZE=3;
    public static final int INSERTTASK_SIZE=1;
    public static final int DEFAULT_TASK_SIZE=1;
    public static final long TOPIC_AMOUNT=3;
    public static final String[] listTopic = {"INFO","DEBUG","ERROR"};
    public static final String PATH = "/home/linhpn/log";
    public static final String PATH_BACKUP="/home/linhpn/backup/";
    public static final boolean REMOVE = true;
}
