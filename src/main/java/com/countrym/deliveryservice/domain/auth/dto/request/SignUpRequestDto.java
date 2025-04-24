package com.countrym.deliveryservice.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식은 ~@~.~를 지켜주세요.")
    @Schema(description = "이메일", example = "aaa@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해 최소 8자, 최대 20자 입력해주세요.")
    @Schema(description = "비밀번호", example = "Qwert!11")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 11, message = "닉네임은 최소 2자에서 최대 11자여야 합니다.")
    @Schema(description = "닉네임", example = "홍길동123")
    private String nickname;

    @Schema(description = "점주 여부", example = "false")
    private boolean owner = false;
}
