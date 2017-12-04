/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entities.Log;
import Helper.LogHelper;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author linhpn
 */
@ManagedBean(name="dashboard", eager = true)
@SessionScoped
public class DashBoardController implements Serializable{

    /**
     * Creates a new instance of DashBoardController
     */
    private Log log; 
    private Map<String,String> logtypes;
    private Map<String,String> services;
    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
    
    public DashBoardController() {
        this.log = new Log();
    }
    public long getEPS(String category) throws IOException{
        return LogHelper.getEPS(category);
    }
    public long getTotal(String category) throws IOException{
        return LogHelper.getTotal(category);
    }
    @PostConstruct
    public void init() {
      
    }
     
    public void displayService() {
        String result="";
        try {
            result = LogHelper.displayResult(log.getUserid(), log.getDate());
        } catch (InterruptedException ex) {
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesMessage msg;
        if(result!=null){
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO,result,""); 
        }
        else{
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"No data is found",""); 
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
}
