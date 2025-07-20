package com.countrym.deliveryservice.domain.store.dto.response;

import com.countrym.deliveryservice.domain.store.entity.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterStoreResponseDto {
    private long storeId;
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

    public static RegisterStoreResponseDto from(Store store) {
        return new RegisterStoreResponseDto(
                store.getId(),
                store.getName(),
                store.getThumbnailUrl(),
                store.getDetails(),
                store.getAddress1(),
                store.getAddress2(),
                store.getOpenAt(),
                store.getClosedAt(),
                store.getMinOrderPrice(),
                store.getTotalOrderCount(),
                store.getAverageRating(),
                store.getTotalReviewCount()
        );
    }
}
