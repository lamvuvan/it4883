/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Config.AppConfig;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author linhpn
 */
public class Test2 {
    public static void main(String[] args) {
        File f = new File("/home/lamvu/schedule.docx");
        System.out.println(f.getName());
        f.renameTo(new File("/home/lamvu/backup/"+f.getName()));
    }
}
