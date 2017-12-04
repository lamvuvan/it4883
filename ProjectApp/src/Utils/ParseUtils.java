/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Config.AppConfig;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author linhpn
 */
public class ParseUtils {
    public static String parse(String type, String input){
        String result ="";
        Matcher m;
        switch(type){
            case "IP":
                m = Pattern.compile(AppConfig.PARSE_IP).matcher(input);
                if(m.find()){
                    result = m.group(1);
                }
                m.reset();
                break;
            case "DATE":
                try{
                m = Pattern.compile(AppConfig.DATE_PATTERN).matcher(input);
                if(m.find()){
                    result = m.group();
                }
                m.reset();
                }catch(Exception e){
                    System.out.println("Date: " + input);
                }
                break;
            case "SERVICE_TYPE":
                m = Pattern.compile(AppConfig.SERVICE_PATTERN).matcher(input);
                if(m.find()){
                    result = m.group(1);
                }
                m.reset();
                break;
        }
        return result;
    }
}
