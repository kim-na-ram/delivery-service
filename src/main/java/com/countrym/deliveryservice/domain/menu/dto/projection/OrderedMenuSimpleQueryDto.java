package com.countrym.deliveryservice.domain.menu.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderedMenuSimpleQueryDto {
    private final long orderId;
    private final long menuId;
    private final String name;
    private final int quantity;

    @QueryProjection
    public OrderedMenuSimpleQueryDto(long orderId, long menuId, String name, int quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.quantity = quantity;
    }
}