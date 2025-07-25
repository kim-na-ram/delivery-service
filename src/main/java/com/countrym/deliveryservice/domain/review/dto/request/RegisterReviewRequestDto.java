package com.countrym.deliveryservice.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterReviewRequestDto {
    @NotNull(message = "별점은 비어있을 수 없습니다.")
    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5점 이하여야 합니다.")
    private Double rating;

    @NotBlank
    @Size(min = 20, message = "최소 20자 이상 작성해야 합니다.")
    private String details;
}
