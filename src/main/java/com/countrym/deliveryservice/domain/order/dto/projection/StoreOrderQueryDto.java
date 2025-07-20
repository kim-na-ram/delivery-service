package com.countrym.deliveryservice.domain.order.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class StoreOrderQueryDto {
    private final long orderId;
    private final long storeOwnerId;
    @Setter
    private List<OrderedMenuSimpleQueryDto> orderedMenuList;
    private final int orderedPrice;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedAt;
    private final LocalDateTime canceledAt;
    private final boolean isReviewed;

    @QueryProjection
    public StoreOrderQueryDto(
            long orderId,
            long storeOwnerId,
            int orderedPrice,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt,
            boolean isReviewed
    ) {
        this.orderId = orderId;
        this.storeOwnerId = storeOwnerId;
        this.orderedPrice = orderedPrice;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.isReviewed = isReviewed;
    }

    @QueryProjection
    public StoreOrderQueryDto(
            long orderId,
            long storeOwnerId,
            List<OrderedMenuSimpleQueryDto> orderedMenuList,
            int orderedPrice,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt,
            boolean isReviewed
    ) {
        this.orderId = orderId;
        this.storeOwnerId = storeOwnerId;
        this.orderedMenuList = orderedMenuList;
        this.orderedPrice = orderedPrice;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.isReviewed = isReviewed;
    }
}
