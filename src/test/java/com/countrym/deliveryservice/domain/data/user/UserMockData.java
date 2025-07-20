package com.countrym.deliveryservice.domain.data.user;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.auth.dto.request.SignUpRequestDto;
import com.countrym.deliveryservice.domain.user.enums.Authority;

public class UserMockData {
    public static UserInfo getUserInfo() {
        return UserInfo.of(1L, Authority.USER);
    }

    public static UserInfo getOwnerInfo() {
        return UserInfo.of(2L, Authority.OWNER);
    }

    public static SignUpRequestDto getUserSignUpRequestDto() {
        return new SignUpRequestDto(
                "user@google.com",
                "user123",
                "user",
                "user",
                false
        );
    }

    public static SignUpRequestDto getUserSignUpRequestDto(String email) {
        return new SignUpRequestDto(
                email,
                "user123",
                "user",
                "user",
                false
        );
    }

    public static SignUpRequestDto getOwnerSignUpRequestDto() {
        return new SignUpRequestDto(
                "owner@google.com",
                "owner123",
                "owner",
                "owner",
                true
        );
    }
}
