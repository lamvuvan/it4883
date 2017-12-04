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
public enum Status {
    
    // Informational 1xx
    _100("100 Continue"),
    _101("101 Switching Protocols"),
    
    // Successful 2xx
    _200("200 OK"),
    
    // Redirection 3xx
    _305("305 Use Proxy"),
    
    // Client Error 4xx
    _400("400 Bad Request"),
    _401("401 Unauthorized"),
    _404("404 Not Found"),
    
    // Server Error 5xx
    _500("500 Internal Server Error"),
    _501("501 Not Implemented"),
    _505("505 HTTP Version Not Supported");

    private String status;

    Status(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
    
}
