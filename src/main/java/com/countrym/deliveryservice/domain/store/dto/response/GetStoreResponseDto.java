package com.countrym.deliveryservice.domain.store.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.response.GetMenuResponseDto;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    public static GetStoreResponseDto from(StoreMenuListDto storeMenuListDto) {
        return new GetStoreResponseDto(
                storeMenuListDto.getName(),
                storeMenuListDto.getThumbnailUrl(),
                storeMenuListDto.getDetails(),
                storeMenuListDto.getAddress1(),
                storeMenuListDto.getAddress2(),
                storeMenuListDto.getOpenAt(),
                storeMenuListDto.getClosedAt(),
                storeMenuListDto.getMinOrderPrice(),
                storeMenuListDto.getAverageRating(),
                storeMenuListDto.getReviewAmount(),
                storeMenuListDto.getMenuList().stream()
                        .filter(menuDto -> StringUtils.hasText(menuDto.getName()))
                        .map(GetMenuResponseDto::from).toList()
        );
    }
}