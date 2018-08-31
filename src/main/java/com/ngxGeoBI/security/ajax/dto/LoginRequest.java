/**
 * Created by : Sukesh Laghate
 * Created on : 10-01-2018
 **/
package com.ngxGeoBI.security.ajax.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginRequest is a DTO that carries the login details
 */
public class LoginRequest {
    private String username;
    private String password;
    private static final Logger logger = LoggerFactory.getLogger(LoginRequest.class);

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username,
                        @JsonProperty("password") String password){
        logger.debug("=============== Login request ========================");
        logger.debug("Username ", username);
        this.username = username;
        this.password = password;

    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
