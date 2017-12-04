/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import Config.DBConfig;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;

/**
 *
 * @author linhpn
 */
public class ConfigurationHelper {
    public static HTable getTable(String tablename) throws IOException{
        Configuration conf = HBaseConfiguration.create();
        HTable table = new HTable(conf, tablename);
        return table;
    }
}
