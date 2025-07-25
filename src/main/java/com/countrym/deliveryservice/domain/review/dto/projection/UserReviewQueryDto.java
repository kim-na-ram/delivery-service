package com.countrym.deliveryservice.domain.review.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserReviewQueryDto {
    private final long reviewId;
    private final long orderId;
    private final String storeName;
    private final double rating;
    private final String details;
    private final LocalDateTime reviewedAt;

    @QueryProjection
    public UserReviewQueryDto(long reviewId, long orderId, String storeName, double rating, String details, LocalDateTime reviewedAt) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.storeName = storeName;
        this.rating = rating;
        this.details = details;
        this.reviewedAt = reviewedAt;
    }
}
