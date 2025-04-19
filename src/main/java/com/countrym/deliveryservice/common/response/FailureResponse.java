package com.countrym.deliveryservice.common.response;

import com.countrym.deliveryservice.common.exception.BaseException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FailureResponse extends BaseResponse {
    LocalDateTime timestamp;

    private FailureResponse(BaseException baseException) {
        super(baseException.getHttpStatus().value(), baseException.getMessage());
        this.timestamp = LocalDateTime.now();
    }

    private FailureResponse(int code, String message) {
        super(code, message);
        this.timestamp = LocalDateTime.now();
    }

    public static FailureResponse from(BaseException baseException) {
        return new FailureResponse(baseException);
    }

    public static FailureResponse of(int code, String message) {
        return new FailureResponse(code, message);
    }
}