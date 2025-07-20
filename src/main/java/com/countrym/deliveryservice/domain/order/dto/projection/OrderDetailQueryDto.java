package com.countrym.deliveryservice.domain.order.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.enums.PaymentMethod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailQueryDto {
    private final long orderId;
    private final long orderUserId;
    private final String orderUserName;
    private final long storeId;
    private final String storeName;
    private final long storeOwnerId;
    private final List<OrderedMenuQueryDto> menuList;
    private final int orderedPrice;
    private final PaymentMethod paymentMethod;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedAt;
    private final LocalDateTime canceledAt;

    @QueryProjection
    public OrderDetailQueryDto(
            long orderId,
            long orderUserId,
            String orderUserName,
            long storeId,
            String storeName,
            long storeOwnerId,
            List<OrderedMenuQueryDto> menuList,
            int orderedPrice,
            PaymentMethod paymentMethod,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt
    ) {
        this.orderId = orderId;
        this.orderUserId = orderUserId;
        this.orderUserName = orderUserName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeOwnerId = storeOwnerId;
        this.menuList = menuList;
        this.orderedPrice = orderedPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
    }
}
