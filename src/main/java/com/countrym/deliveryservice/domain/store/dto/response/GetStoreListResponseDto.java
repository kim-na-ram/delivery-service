package com.countrym.deliveryservice.domain.store.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetStoreListResponseDto {
    private long storeId;
    private String name;
    private String thumbnailUrl;
    private boolean openStatus;
    private int minOrderPrice;
    private double averageRating;
    private int reviewAmount;

    @QueryProjection
    public GetStoreListResponseDto(
            long storeId,
            String name,
            String thumbnailUrl,
            LocalTime openAt,
            LocalTime closedAt,
            int minOrderPrice,
            double averageRating,
            int reviewAmount
    ) {
        this.storeId = storeId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.openStatus = checkIsOpen(LocalTime.now(), openAt, closedAt);
        this.minOrderPrice = minOrderPrice;
        this.averageRating = averageRating;
        this.reviewAmount = reviewAmount;
    }

    private boolean checkIsOpen(LocalTime now, LocalTime openAt, LocalTime closedAt) {
        boolean isOpen;
        if (closedAt.getHour() <= 6 && now.getHour() <= 6) {
            isOpen = now.isBefore(closedAt);
        } else if(closedAt.getHour() <= 6) {
            isOpen = now.isAfter(openAt);
        } else {
            isOpen = !now.isBefore(openAt) && !now.isAfter(closedAt);
        }

        return isOpen;
    }
}