package com.countrym.deliveryservice.domain.user.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;

public enum Grade implements BaseEnum {
    BRONZE("브론즈"),
    SILVER("실버"),
    GOLD("골드"),
    DIAMOND("다이아몬드");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    Grade(String description) {
        this.description = description;
    }
}
