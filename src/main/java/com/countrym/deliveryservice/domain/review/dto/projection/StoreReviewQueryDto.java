package com.countrym.deliveryservice.domain.review.dto.projection;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class StoreReviewQueryDto {
    private final long reviewId;
    private final long orderId;
    private final String reviewerNickname;
    @Setter
    private List<OrderedMenuSimpleQueryDto> orderedMenuList;
    private final double rating;
    private final String details;
    private final LocalDateTime reviewedAt;

    @QueryProjection
    public StoreReviewQueryDto(long reviewId, long orderId, String reviewerNickname, double rating, String details, LocalDateTime reviewedAt) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.reviewerNickname = reviewerNickname;
        this.rating = rating;
        this.details = details;
        this.reviewedAt = reviewedAt;
    }
}