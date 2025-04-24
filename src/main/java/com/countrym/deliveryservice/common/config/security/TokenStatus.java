package com.countrym.deliveryservice.common.config.security;

import com.countrym.deliveryservice.common.enums.BaseEnum;

public enum TokenStatus implements BaseEnum {
    USUAL("정상"),
    UNUSUAL("비정상")
    ;

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    TokenStatus(String description) {
        this.description = description;
    }
}