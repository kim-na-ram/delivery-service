package com.countrym.deliveryservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS("정상 처리되었습니다."),
    INVALID_TIMEOUT("다시 시도해주세요."),
    FORBIDDEN("접근 권한이 없습니다."),

    ;

    private final String message;
}
