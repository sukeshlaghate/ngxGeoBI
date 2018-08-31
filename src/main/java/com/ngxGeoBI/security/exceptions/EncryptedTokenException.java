/**
 * Created by : Sukesh Laghate
 * Created on : 09-01-2018
 **/
package com.ngxGeoBI.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class EncryptedTokenException extends AuthenticationServiceException {

    public EncryptedTokenException(String msg){
        super(msg);
    }

}
