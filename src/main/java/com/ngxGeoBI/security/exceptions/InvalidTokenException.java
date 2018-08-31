/**
 * Created by : Sukesh Laghate
 * Created on : 09-01-2018
 **/
package com.ngxGeoBI.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class InvalidTokenException extends AuthenticationServiceException {

    public InvalidTokenException(String msg){
        super(msg);
    }

}
