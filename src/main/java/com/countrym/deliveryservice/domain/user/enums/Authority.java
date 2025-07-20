package com.countrym.deliveryservice.domain.user.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;

import java.util.Arrays;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_USER_AUTHORITY;

public enum Authority implements BaseEnum {
    USER("사용자"),
    OWNER("점주");

    private final String description;

    @Override
    public String getDescription() {
        return description;
    }

    Authority(String description) {
        this.description = description;
    }

    public static Authority from(boolean isOwner) {
        return isOwner ? OWNER : USER;
    }

    public boolean isOwner() {
        return this == OWNER;
    }

    public boolean isUser() {
        return this == USER;
    }

    public static Authority of(String role) {
        return Arrays.stream(Authority.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidParameterException(NOT_FOUND_USER_AUTHORITY));
    }
}
