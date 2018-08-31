/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.common;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final ErrorCode errorCode;
    private final Date timeStamp;

    protected  ErrorResponse(final String message, final ErrorCode errorCode,
                             final HttpStatus status){
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timeStamp = new Date();
    }

    public static  ErrorResponse of(final String message,
                                    final ErrorCode errorCode, HttpStatus status){
        return new ErrorResponse(message, errorCode, status);
    }

    public Integer getStatus() { return status.value();}

    public String getMessage() { return message;}

    public ErrorCode getErrorCode() {return errorCode;}

    public Date getTimeStamp() {return timeStamp;}

    /* Returns the string representation of this Error Response.
       The format of string is "Error Code: + Message" where Re is real part
       and Im is imagenary part.*/
    @Override
    public String toString() {
        return String.format(this.errorCode + ":\n" + this.message);
    }

}
