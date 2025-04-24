package com.countrym.deliveryservice.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends BaseException {
    public InvalidParameterException(ResponseCode responseCode) {
        super(HttpStatus.BAD_REQUEST, responseCode.getMessage());
    }
}