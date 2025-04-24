package com.countrym.deliveryservice.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SignInRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "aaa@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "Qwert!11")
    private String password;
}
