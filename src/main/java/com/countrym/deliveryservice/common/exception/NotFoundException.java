package com.countrym.deliveryservice.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ResponseCode responseCode) {
        super(HttpStatus.NOT_FOUND, responseCode.getMessage());
    }
}
