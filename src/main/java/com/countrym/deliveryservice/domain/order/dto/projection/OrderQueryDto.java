package com.countrym.deliveryservice.domain.order.dto.projection;

import com.countrym.deliveryservice.domain.order.entity.Order;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderQueryDto {
    private final long storeId;
    private final long ownerId;
    private final long orderUserId;
    private final Order order;

    @QueryProjection
    public OrderQueryDto(long storeId, long ownerId, long orderUserId, Order order) {
        this.storeId = storeId;
        this.ownerId = ownerId;
        this.orderUserId = orderUserId;
        this.order = order;
    }
}
