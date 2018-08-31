/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {
    public BadCredentialsException(String msg) {
        super(msg);
    }
}
