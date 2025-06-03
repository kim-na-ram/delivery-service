package com.countrym.deliveryservice.domain.store.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.MenuDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreMenuListDto {
    private String name;
    private String thumbnailUrl;
    private String details;
    private String address1;
    private String address2;
    private LocalTime openAt;
    private LocalTime closedAt;
    private int minOrderPrice;
    private double averageRating;
    private int reviewAmount;
    private List<MenuDto> menuList;

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
            double averageRating,
            int reviewAmount,
            List<MenuDto> menuList
    ) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.details = details;
        this.address1 = address1;
        this.address2 = address2;
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.minOrderPrice = minOrderPrice;
        this.averageRating = averageRating;
        this.reviewAmount = reviewAmount;
        this.menuList = menuList;
    }
}
