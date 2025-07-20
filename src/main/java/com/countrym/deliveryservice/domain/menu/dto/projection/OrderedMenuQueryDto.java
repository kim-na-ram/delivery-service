package com.countrym.deliveryservice.domain.menu.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OrderedMenuQueryDto {
    private final long menuId;
    private final String name;
    private final Integer price;
    private final int quantity;

    @QueryProjection
    public OrderedMenuQueryDto(long menuId, String name, int price, int quantity) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
