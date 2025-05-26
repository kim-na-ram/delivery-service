package com.countrym.deliveryservice.domain.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ModifyStoreRequestDto {
    @Schema(description = "가게명", example = "피자스쿨 OO점")
    private String name;

    @Schema(description = "가게 썸네일 이미지", example = "thumbnail.png")
    private String thumbnailUrl;

    @Schema(description = "가게 설명", example = "피자스쿨 OO점을 찾아주신 고객분들께 감사합니다.")
    private String details;

    @Schema(description = "가게 주소", example = "서울 송파구 위례성대로 2")
    private String address1;

    @Schema(description = "가게 상세 주소", example = "1층")
    private String address2;

    @Schema(description = "가게 타입", example = "피자·양식")
    private String type;

    @Schema(description = "가게 오픈 시간", example = "10:00")
    private LocalTime openAt;

    @Schema(description = "가게 마감 시간", example = "22:00")
    private LocalTime closedAt;

    @Min(value = 0, message = "최소 주문 가격은 0원 이상이어야 합니다.")
    @Schema(description = "가게 최소 주문 가격", example = "1000")
    private Integer minOrderPrice;
}
