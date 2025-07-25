package com.countrym.deliveryservice.domain.review.repository;

import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_REVIEW;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
    default ReviewQueryDto findByOrderIdAndReviewId(long orderId, long reviewId) {
        return findByOrderIdAndId(orderId, reviewId)
                .orElseThrow(() -> new InvalidParameterException(NOT_FOUND_REVIEW));
    }
}
