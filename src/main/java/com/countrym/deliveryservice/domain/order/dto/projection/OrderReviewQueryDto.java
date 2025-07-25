package com.countrym.deliveryservice.domain.order.dto.projection;

import com.countrym.deliveryservice.domain.order.entity.Order;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderReviewQueryDto {
    private final long orderUserId;
    private final long storeId;
    private final Order order;

    @QueryProjection
    public OrderReviewQueryDto(long orderUserId, long storeId, Order order) {
        this.orderUserId = orderUserId;
        this.storeId = storeId;
        this.order = order;
    }
}
