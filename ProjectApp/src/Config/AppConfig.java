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
    public static final String FILE_NAME_PATTERN ="log.txt.(.)*[012]$";
    public static final String FILE_NAME_PATTERN2 ="log.txt.(.)*[345]$";
    public static final String FILE_NAME_PATTERN3 ="log.txt.(.)*[6789]$";
    public static final String FILE_NAME_PATTERN4 ="log.txt.(.)*[67]$";
    public static final String FILE_NAME_PATTERN5 ="log.txt.(.)*[89]$";

    public static final String IP_PATTERN ="((\\d+).(\\d+).(\\d+).(\\d+))";
    public static final String PARSE_IP="IP:((\\d+).(\\d+).(\\d+).(\\d+))";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN="((19|20)\\d\\d-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]))";
    public static final String SERVICE_PATTERN="Code:(\\d+)";
    public static final String INFO = "INFO";
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";
    public static final long LIMIT_QUEUE_SIZE=20000;
    public static final long COMMIT_MINIMUN_SIZE=4000;
    public static final String [] topics = {"INFOPUT","WARNINGPUT","ERRORPUT"};
    public static final int SENDTASKS_SIZE=1;
    public static final int RECEIVETASK_SIZE=3;
    public static final int INSERTTASK_SIZE=2;
    public static final long TOPIC_AMOUNT=3;
    public static final String PATH = "/home/lamvu/workspace/Server/log";
    public static final boolean BACKUP = true;
    public static final String PATH_BACKUP="/home/lamvu/backup/";
}
