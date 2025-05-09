package com.countrym.deliveryservice.domain.menu.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterMenuRequestDto {
    @NotBlank
    @Schema(description = "메뉴명", example = "콤비네이션피자")
    private String name;

    @Schema(description = "메뉴 썸네일 이미지", example = "thumbnail.png")
    private String thumbnailUrl;

    @Schema(description = "메뉴 설명", example = "피자의 대명사! 콤비네이션 피자")
    private String details;

    @Min(value = 0, message = "메뉴 가격은 0원 이상이어야 합니다.")
    @Schema(description = "메뉴 가격", example = "13000")
    private Integer price;
}
