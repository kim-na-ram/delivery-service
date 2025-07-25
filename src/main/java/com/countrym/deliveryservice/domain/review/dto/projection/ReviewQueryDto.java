package com.countrym.deliveryservice.domain.review.dto.projection;

import com.countrym.deliveryservice.domain.review.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ReviewQueryDto {
    private final long reviewerId;
    private final long storeId;
    private final Review review;

    @QueryProjection
    public ReviewQueryDto(long reviewerId, long storeId, Review review) {
        this.reviewerId = reviewerId;
        this.storeId = storeId;
        this.review = review;
    }
}
