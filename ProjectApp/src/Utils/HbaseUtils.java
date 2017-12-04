/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Config.Hbase;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author linhpn
 */
public class HbaseUtils {
    public static byte[] getRow(Object... obs){
        byte[] rowkey = new byte[20];
        for(Object ob: obs){
            if(ob.getClass().getName().equals(String.class.getName())){
                rowkey=Bytes.toBytes(ob.toString());
            }
            else if(ob.getClass().getName().equals(long.class.getName())){
                rowkey=Bytes.add(rowkey,Bytes.toBytes(Long.parseLong(ob.toString())));
            }
            else if(ob.getClass().getName().equals(int.class.getName())){
                rowkey = Bytes.add(rowkey,Bytes.toBytes(Integer.parseInt(ob.toString())));
            }
        }
        return rowkey;
    }
    public static void createTable() throws IOException{
        Configuration conf = HBaseConfiguration.create();
        HConnection conn = (HConnection) ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        HTableDescriptor table = new HTableDescriptor(Hbase.TABLE_LOG);
        HColumnDescriptor family = new HColumnDescriptor(Hbase.COLFAM);
        table.addFamily(family);
        byte[][] regions = new byte[10][10];
        for(int i=0;i<10;i++){
            regions[i] = Bytes.toBytes(String.valueOf(i));
        }
        admin.createTable(table,regions);
        System.out.println("Create table completely");
    }
}
