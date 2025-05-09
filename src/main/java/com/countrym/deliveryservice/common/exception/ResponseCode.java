package com.countrym.deliveryservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS("정상 처리되었습니다."),
    INVALID_TIMEOUT("다시 시도해주세요."),
    FORBIDDEN("접근 권한이 없습니다."),

    // auth
    NOT_FOUND_USER("해당 사용자는 존재하지 않습니다."),
    NOT_FOUND_USER_AUTHORITY("해당 사용자 권한은 존재하지 않습니다."),
    INVALID_EMAIL_OR_PASSWORD("이메일 혹은 비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    ALREADY_SIGNED_EMAIL_ERROR("이미 가입된 이메일입니다."),

    // store
    NOT_FOUND_STORE("해당 가게는 존재하지 않습니다."),
    INVALID_USER_AUTHORITY("점주만이 가능한 기능입니다."),
    INVALID_STORE_TYPE("유효하지 않은 가게 타입입니다."),
    INVALID_STORE_OWNER("해당 가게의 점주가 아닙니다."),
    ALREADY_EXISTS_STORE_ERROR("이미 존재하는 가게명입니다."),

    // menu
    NOT_FOUND_MENU("해당 메뉴는 존재하지 않습니다."),
    ALREADY_EXISTS_MENU_ERROR("이미 존재하는 메뉴명입니다.");

    private final String message;
}
