package com.countrym.deliveryservice.domain.menu.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StoreOwnerMenuDto {
    private final long storeId;
    private final long ownerId;
    private final long menuId;
    private final String name;
    private final String thumbnailUrl;
    private final String details;
    private final int price;

    @QueryProjection
    public StoreOwnerMenuDto(
            long storeId,
            long ownerId,
            long menuId,
            String name,
            String thumbnailUrl,
            String details,
            int price
    ) {
        this.storeId = storeId;
        this.ownerId = ownerId;
        this.menuId = menuId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.details = details;
        this.price = price;
    }
}
