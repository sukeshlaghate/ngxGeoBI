/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    REGISTRATION(-2),
    GLOBAL(2),
    AUTHENTICATION(10),
    JWT_TOKEN_EXPIRED(11);

    private int errorCode;

    private  ErrorCode(int errorCode){
        this.errorCode = errorCode;
    }
    @JsonValue
    public int getErrorCode(){
        return errorCode;
    }
}
