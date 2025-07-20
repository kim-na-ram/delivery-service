package com.countrym.deliveryservice.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ModifyOrderStatusRequestDto {
    @NotBlank(message = "주문 변경 시, 상태는 필수입니다.")
    private String orderStatus;
}
