package com.countrym.deliveryservice.domain.review.repository;

import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.StoreReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.UserReviewQueryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryRepository {
    Optional<ReviewQueryDto> findByOrderIdAndId(long orderId, long reviewId);

    List<StoreReviewQueryDto> findByStoreIdAndPaging(long storeId, Pageable pageable);

    List<UserReviewQueryDto> findByUserIdAndPaging(long userId, Pageable pageable);
}
