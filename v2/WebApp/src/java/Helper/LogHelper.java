/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import Config.DBConfig;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author linhpn
 */
public class LogHelper implements Serializable{
    public static long getEPS(String category) throws IOException{
        HTable table = ConfigurationHelper.getTable(DBConfig.TABLE_ANALYZE);
        long EPS=0;
        Result rs;
        if(category.equals("TOTAL")){
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            rs =null;
            while((rs=scanner.next())!=null){
                String rowkey = Bytes.toString(rs.getRow());
                if(rowkey.equals(DBConfig.SERVER1)||rowkey.equals(DBConfig.SERVER2)||rowkey.equals(DBConfig.SERVER3)){
                    EPS+=Bytes.toLong(rs.getValue(Bytes.toBytes(DBConfig.COLFAM), Bytes.toBytes(DBConfig.EPS)));
                }
            }
        }
        else{
            Get g = new Get(Bytes.toBytes(category));
//            System.out.println("Category: "+category);
            rs = table.get(g);
            EPS = Bytes.toLong(rs.getValue(Bytes.toBytes(DBConfig.COLFAM), Bytes.toBytes(DBConfig.EPS)));
        }
        return EPS;
    }
    public static long getTotal(String category) throws IOException{
        HTable table = ConfigurationHelper.getTable(DBConfig.TABLE_ANALYZE);
        long total=0;
        Result rs;
        if(category.equals("TOTAL")){
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            rs =null;
            while((rs=scanner.next())!=null){
                String rowkey = Bytes.toString(rs.getRow());
                if(rowkey.equals(DBConfig.INFO)||rowkey.equals(DBConfig.WARNING)||rowkey.equals(DBConfig.ERROR)){
                    total+=Bytes.toLong(rs.getValue(Bytes.toBytes(DBConfig.COLFAM), Bytes.toBytes(DBConfig.AMOUNT)));
                }
            }
        }
        else{
            Get g = new Get(Bytes.toBytes(category));
            rs = table.get(g);
            total = Bytes.toLong(rs.getValue(Bytes.toBytes(DBConfig.COLFAM), Bytes.toBytes(DBConfig.AMOUNT)));
        }
        return total;
    }
    public static byte[] parseIP(String ip){
        byte[] key;
        Matcher m;
        m = Pattern.compile(DBConfig.IP_PATTERN).matcher(ip);
        if(m.matches()){
            key = Bytes.toBytes(Integer.parseInt(m.group(2)));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(3))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(4))));
            key = Bytes.add(key,Bytes.toBytes(Integer.parseInt(m.group(5))));
            return key;
        }
        return null;
    }
    public static String displayResult(String ip, Date date) throws InterruptedException, ParseException, IOException{
        String result =" ";
        HTable table = ConfigurationHelper.getTable(DBConfig.TABLE_LOG);
        byte[] rowkey = Bytes.toBytes(ip.substring(ip.length()-1));
        rowkey = Bytes.add(rowkey,Bytes.toBytes(date.getTime()));
        rowkey = Bytes.add(rowkey,parseIP(ip));
        Get g = new Get(rowkey);
        Result rs = table.get(g);
        if(rs!=null){
            result = (Bytes.toString(rs.getValue(Bytes.toBytes(DBConfig.COLFAM), Bytes.toBytes(DBConfig.QUALIFILTER_LOG))));
        }
//        System.out.println("Result: "+result);
        return result;
    }
    
    
}
