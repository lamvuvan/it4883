/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Object.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Serializer;

/**
 *
 * @author linhpn
 */
public class LogSerializer implements Serializer<Log>{
    private ObjectMapper ob;
    @Override
    public void configure(Map<String, ?> map, boolean bln) {
        ob = new ObjectMapper();
    }

    @Override
    public byte[] serialize(String string, Log t) {
        byte[] retVal =null;
        try{
            retVal = ob.writeValueAsBytes(t);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(LogSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    @Override
    public void close() {
        
    }
}
