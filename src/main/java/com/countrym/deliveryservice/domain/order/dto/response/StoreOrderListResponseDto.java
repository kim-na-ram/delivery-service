package com.countrym.deliveryservice.domain.order.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.response.OrderedMenuResponseDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class StoreOrderListResponseDto {
    private final long orderId;
    private final List<OrderedMenuResponseDto> orderedMenuList;
    private final int orderedPrice;
    private final String orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime canceledAt;
    private boolean isReviewed;

    private StoreOrderListResponseDto(
            long orderId,
            List<OrderedMenuResponseDto> orderedMenuList,
            int orderedPrice,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt,
            boolean isReviewed
    ) {
        this.orderId = orderId;
        this.orderedMenuList = orderedMenuList;
        this.orderedPrice = orderedPrice;
        this.orderStatus = orderStatus.getDescription();
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.isReviewed = isReviewed;
    }

    public static StoreOrderListResponseDto from(StoreOrderQueryDto storeOrderQueryDto) {
        return new StoreOrderListResponseDto(
                storeOrderQueryDto.getOrderId(),
                storeOrderQueryDto.getOrderedMenuList().stream().map(OrderedMenuResponseDto::from).toList(),
                storeOrderQueryDto.getOrderedPrice(),
                storeOrderQueryDto.getOrderStatus(),
                storeOrderQueryDto.getOrderedAt(),
                storeOrderQueryDto.getCanceledAt(),
                storeOrderQueryDto.isReviewed()
        );
    }
}