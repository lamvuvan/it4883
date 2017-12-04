/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Config.AppConfig;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author linhpn
 */
public class Test2 {
    public static void main(String[] args) {
        String ip ="IP:210.189.77.78";
        byte[] key;
        Matcher m;
        m = Pattern.compile(AppConfig.IP_PATTERN).matcher(ip);
        if(m.matches()){
            key = Bytes.toBytes(Integer.parseInt(m.group(2)));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(3))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(4))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(5))));
            System.out.println(Bytes.toStringBinary(key));
        }
    }
}
