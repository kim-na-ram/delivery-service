package com.countrym.deliveryservice.domain.store.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.MenuQueryDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreMenuListDto {
    private String name;
    private String thumbnailUrl;
    private String details;
    private String address1;
    private String address2;
    private LocalTime openAt;
    private LocalTime closedAt;
    private int minOrderPrice;
    private int totalOrderCount;
    private double averageRating;
    private int totalReviewCount;
    private List<MenuQueryDto> menuList;

    @QueryProjection
    public StoreMenuListDto(
            String name,
            String thumbnailUrl,
            String details,
            String address1,
            String address2,
            LocalTime openAt,
            LocalTime closedAt,
            int minOrderPrice,
            int totalOrderCount,
            double averageRating,
            int totalReviewCount,
            List<MenuQueryDto> menuList
    ) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.details = details;
        this.address1 = address1;
        this.address2 = address2;
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.minOrderPrice = minOrderPrice;
        this.totalOrderCount = totalOrderCount;
        this.averageRating = averageRating;
        this.totalReviewCount = totalReviewCount;
        this.menuList = menuList;
    }
}
