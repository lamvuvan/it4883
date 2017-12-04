/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Object.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Deserializer;

/**
 *
 * @author linhpn
 */
public class LogDeserializer implements Deserializer<Log>{
    private ObjectMapper ob;
    @Override
    public void configure(Map<String, ?> map, boolean bln) {
        ob = new ObjectMapper();
    }

    @Override
    public Log deserialize(String string, byte[] bytes) {
        Log msg = null;
        try{
            msg = (Log) ob.readValue(bytes, Log.class);
        } catch (IOException ex) {
            Logger.getLogger(LogDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    @Override
    public void close() {
        
    }
    
}
