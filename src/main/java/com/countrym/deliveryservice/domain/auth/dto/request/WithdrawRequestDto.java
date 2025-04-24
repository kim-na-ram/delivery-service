package com.countrym.deliveryservice.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class WithdrawRequestDto {
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "Qwert!11")
    private String password;
}
