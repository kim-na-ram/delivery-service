package com.countrym.deliveryservice.domain.store.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;

import java.util.Arrays;

import static com.countrym.deliveryservice.common.exception.ResponseCode.INVALID_STORE_TYPE;

public enum StoreType implements BaseEnum {
    CAFE("카페"),
    KOREAN_FOOD("한식"),
    CHICKEN("치킨"),
    CHINESE_FOOD("중식"),
    SNACK_FOOD("분식"),
    BOSSAM("족발·보쌈"),
    WESTERN_FOOD("피자·양식"),
    STEW("찜·탕"),
    JAPANESE_FOOD("일식·돈가스"),
    ASIAN_FOOD("아시안"),
    FRANCHISE("프랜차이즈"),
    PICK_UP("픽업"),
    CONVENIENCE_OR_MART("편의점·마트");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    StoreType(String description) {
        this.description = description;
    }

    public static StoreType of(String type) {
        return Arrays.stream(StoreType.values())
                .filter(t -> t.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException(INVALID_STORE_TYPE));
    }
}
