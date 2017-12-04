/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;

/**
 *
 * @author linhpn
 */
public class ConfigurationUtils {
    private static Configuration conf;
    public static HTable getHTable(String name) throws IOException{
        if(conf==null){
            conf = HBaseConfiguration.create();
        }
        HTable table = new HTable(conf, name);
        return table;
    }
}
