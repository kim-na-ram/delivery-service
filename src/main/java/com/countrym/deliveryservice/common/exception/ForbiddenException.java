package com.countrym.deliveryservice.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ResponseCode responseCode) {
        super(HttpStatus.FORBIDDEN, responseCode.getMessage());
    }
}
