package com.countrym.deliveryservice.domain.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "가게 ID는 필수입니다.")
    private Long storeId;

    @Valid
    @NotNull(message = "주문할 메뉴 목록은 필수입니다.")
    @Size(min = 1, message = "메뉴는 최소 1개 이상이어야 합니다.")
    private List<OrderMenuRequestDto> menuList;

    @NotNull(message = "총 주문 가격은 필수입니다.")
    @Min(value = 0, message = "총 주문 가격은 0원보다 낮을 수 없습니다.")
    private Integer orderedPrice;

    @NotBlank(message = "결제 방법은 필수입니다.")
    private String paymentMethod;

    @Getter
    @AllArgsConstructor
    public static class OrderMenuRequestDto {
        @NotNull(message = "메뉴 ID는 필수입니다.")
        private Long menuId;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
        private Integer quantity;
    }
}
