package com.countrym.deliveryservice.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateElementException extends BaseException {
    public DuplicateElementException(ResponseCode responseCode) {
        super(HttpStatus.CONFLICT, responseCode.getMessage());
    }
}
