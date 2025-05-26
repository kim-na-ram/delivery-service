package com.countrym.deliveryservice.domain.store.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.response.GetMenuResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetStoreResponseDto {
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
    private List<GetMenuResponseDto> menuList;

    public static GetStoreResponseDto from(Store store) {
        return new GetStoreResponseDto(
                store.getName(),
                store.getThumbnailUrl(),
                store.getDetails(),
                store.getAddress1(),
                store.getAddress2(),
                store.getOpenAt(),
                store.getClosedAt(),
                store.getMinOrderPrice(),
                store.getAverageRating(),
                store.getReviewAmount(),
                store.getMenuList().stream().map(GetMenuResponseDto::from).toList()
        );
    }
}