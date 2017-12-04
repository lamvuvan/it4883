/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.http;

/**
 *
 * @author lamvu
 */
public enum Method {
    
    GET("GET"), 
    POST("POST"), 
    PUT("PUT"), 
    DELETE("DELETE"), 
    UNRECOGNIZED(null);
    
    private String method;
    
    Method(String method){
        this.method = method;
    }
}
