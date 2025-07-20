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
    INVALID_WITHDRAW_USER("모든 주문이 완료된 이후에 회원탈퇴가 가능합니다."),
    INVALID_WITHDRAW_OWNER("모든 가게를 폐업 처리한 이후에 회원탈퇴가 가능합니다."),
    ALREADY_SIGNED_EMAIL_ERROR("이미 가입된 이메일입니다."),

    // store
    NOT_FOUND_STORE("해당 가게는 존재하지 않습니다."),
    INVALID_USER_AUTHORITY("점주만이 가능한 기능입니다."),
    INVALID_STORE_TYPE("유효하지 않은 가게 타입입니다."),
    INVALID_STORE_OWNER("해당 가게의 점주가 아닙니다."),
    INVALID_STORE_STATUS("현재 영업 종료된 가게입니다."),
    INVALID_UNCOMPLETED_ORDER_IN_STORE("가게를 폐업하기 전에 모든 주문을 완료해야만 합니다."),
    ALREADY_EXISTS_STORE_ERROR("이미 존재하는 가게명입니다."),

    // menu
    NOT_FOUND_MENU("해당 메뉴는 존재하지 않습니다."),
    NOT_FOUND_STORE_MENU("해당 가게에 존재하지 않는 메뉴입니다."),
    ALREADY_EXISTS_MENU_ERROR("이미 존재하는 메뉴명입니다."),

    // order
    NOT_FOUND_ORDER("해당 주문은 존재하지 않습니다."),
    NOT_FOUND_ORDER_LIST_IN_THREE_MONTH("최근 3개월 이내 주문 내역이 존재하지 않습니다"),
    UNAUTHORIZED_CHANGE_ORDER_STATUS("주문 상태 변경은 사장님만 가능합니다."),
    INVALID_ORDERED_PRICE("총 주문 가격이 올바르지 않습니다."),
    INVALID_PAYMENT_METHOD("해당 결제 수단은 지원되지 않습니다."),
    INVALID_ORDER_TIME("현재 영업 준비중이기에 주문이 불가능합니다."),
    INVALID_MINIMAL_ORDER_PRICE("최소 주문 금액을 만족해야 합니다."),
    INVALID_ORDER_STATUS("해당 주문 상태는 유효하지 않습니다."),
    INVALID_CHANGE_ORDER_STATUS("이전 주문 상태로 돌아갈 수 없습니다."),
    INVALID_CHANGE_SAME_ORDER_STATUS("현재 주문 상태와 동일합니다."),
    INVALID_CHANGE_ORDER_STATUS_FROM_CANCEL("취소된 주문의 상태는 변경 할 수 없습니다."),
    INVALID_CHANGE_ORDER_STATUS_TO_CANCEL("주문 상태 변경으로는 주문 취소를 할 수 없습니다."),
    INVALID_CANCEL_ORDER_FOR_USER("주문 취소는 주문이 수락되기 전에만 가능합니다."),
    INVALID_CANCEL_ORDER_FOR_OWNER("주문 취소는 주문을 수락하기 전에만 가능합니다."),
    ALREADY_CANCELED_ORDER_ERROR("이미 취소된 주문입니다");

    private final String message;
}
