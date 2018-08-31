/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security.exceptions;


import org.springframework.security.core.AuthenticationException;

public class JwtExpiredTokenException extends AuthenticationException {
    private String token;
    public JwtExpiredTokenException(String msg) {
        super(msg);
    }
    public  JwtExpiredTokenException(String token, String msg, Throwable t){
        super(msg, t);
        this.token = token;
    }

    public String token(){ return this.token;}
}
