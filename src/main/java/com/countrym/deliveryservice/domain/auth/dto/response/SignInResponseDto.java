package com.countrym.deliveryservice.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInResponseDto {
    private String accessToken;

    public static SignInResponseDto of(String accessToken) {
        return new SignInResponseDto(accessToken);
    }
}
