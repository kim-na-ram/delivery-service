package com.countrym.deliveryservice.domain.auth.controller;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.response.SuccessResponse;
import com.countrym.deliveryservice.domain.auth.dto.request.SignInRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.SignUpRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.WithdrawRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.response.SignInResponseDto;
import com.countrym.deliveryservice.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원 가입 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = SignUpRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<Void>> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        authService.signUp(signUpRequestDto);
        return ResponseEntity.ok(SuccessResponse.of());
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = SignInRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<SignInResponseDto>> signIn(
            @Valid @RequestBody SignInRequestDto signInRequestDto
    ) {
        String accessToken = authService.signIn(signInRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(SignInResponseDto.of(accessToken)));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = WithdrawRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<Void>> withdraw(
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody WithdrawRequestDto withdrawRequestDto
    ) {
        authService.withdraw(userInfo.getId(), withdrawRequestDto);
        return ResponseEntity.ok(SuccessResponse.of());
    }
}
