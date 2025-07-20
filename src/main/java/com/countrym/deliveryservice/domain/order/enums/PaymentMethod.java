package com.countrym.deliveryservice.domain.order.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;

import java.util.Arrays;

import static com.countrym.deliveryservice.common.exception.ResponseCode.INVALID_PAYMENT_METHOD;

public enum PaymentMethod implements BaseEnum {
    CARD("신용/체크카드"),
    NAVER_PAY("네이버페이"),
    KAKAO_PAY("카카오페이"),
    SAMSUNG_PAY("삼성페이");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static PaymentMethod of(String name) {
        return Arrays.stream(PaymentMethod.values())
                .filter(p -> p.name().equals(name))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException(INVALID_PAYMENT_METHOD));
    }

//    public static PaymentMethod of(String description) {
//        return Arrays.stream(PaymentMethod.values())
//                .filter(p -> p.getDescription().equals(description))
//                .findAny()
//                .orElseThrow(() -> new InvalidParameterException(INVALID_PAYMENT_METHOD));
//    }
}
