/**
 * Created by : Sukesh Laghate
 * Created on : 10-01-2018
 **/
package com.ngxGeoBI.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
