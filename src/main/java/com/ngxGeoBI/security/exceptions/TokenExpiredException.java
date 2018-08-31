/**
 * Created by : Sukesh Laghate
 * Created on : 10-01-2018
 **/
package com.ngxGeoBI.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class TokenExpiredException extends AuthenticationServiceException {

    public TokenExpiredException(String msg) {
        super(msg);
    }
}
