package com.countrym.deliveryservice.domain.menu.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MenuDto {
    private final Long menuId;
    private final String name;
    private final String thumbnailUrl;
    private final String details;
    private final Integer price;

    @QueryProjection
    public MenuDto(long menuId, String name, String thumbnailUrl, String details, int price) {
        this.menuId = menuId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.details = details;
        this.price = price;
    }
}
