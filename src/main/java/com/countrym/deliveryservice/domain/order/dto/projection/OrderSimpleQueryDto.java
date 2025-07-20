package com.countrym.deliveryservice.domain.order.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class OrderSimpleQueryDto {
    private final long orderId;
    private final long storeId;
    private final String storeName;
    @Setter
    private List<OrderedMenuSimpleQueryDto> orderedMenuList;
    private final int orderedPrice;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedAt;
    private final LocalDateTime canceledAt;
    private final boolean isReviewed;

    @QueryProjection
    public OrderSimpleQueryDto(
            long orderId,
            long storeId,
            String storeName,
            int orderedPrice,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt,
            boolean isReviewed
    ) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderedPrice = orderedPrice;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.isReviewed = isReviewed;
    }

    @QueryProjection
    public OrderSimpleQueryDto(
            long orderId,
            long storeId,
            String storeName,
            List<OrderedMenuSimpleQueryDto> orderedMenuList,
            int orderedPrice,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt,
            boolean isReviewed
    ) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderedMenuList = orderedMenuList;
        this.orderedPrice = orderedPrice;
        this.orderStatus = orderStatus;
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
        this.isReviewed = isReviewed;
    }
}
