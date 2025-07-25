package com.countrym.deliveryservice.domain.review.service;

import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderReviewQueryDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.request.RegisterReviewRequestDto;
import com.countrym.deliveryservice.domain.review.dto.response.GetMyReviewListResponseDto;
import com.countrym.deliveryservice.domain.review.dto.response.GetStoreReviewListResponseDto;
import com.countrym.deliveryservice.domain.review.entity.Review;
import com.countrym.deliveryservice.domain.review.facade.ReviewFacade;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewFacade reviewFacade;

    @Transactional
    public void registerReview(long userId, long orderId, RegisterReviewRequestDto registerReviewRequestDto) {
        OrderReviewQueryDto orderReviewQueryDto = reviewFacade.getOrder(orderId);

        if (orderReviewQueryDto.getOrderUserId() != userId) {
            throw new NotFoundException(NOT_FOUND_ORDER);
        }

        Order order = orderReviewQueryDto.getOrder();

        if (!OrderStatus.isCompleted(order.getStatus())) {
            throw new InvalidParameterException(INVALID_REVIEW_NOT_COMPLETED_ORDER);
        }

        // 3일 이내의 주문에 대해서만 리뷰 작성 가능
        if (orderReviewQueryDto.getOrder().getOrderedAt().plusDays(3).isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException(INVALID_REVIEW_ORDER_DATE_OVER_THREE_DAYS);
        }

        Store store = reviewFacade.getStore(orderReviewQueryDto.getStoreId());
        store.registeredReview(registerReviewRequestDto.getRating(), 1);

        User user = User.of(userId);
        Review review = Review.from(user, order, store, registerReviewRequestDto);

        reviewFacade.saveReview(store, review);
    }

    @Transactional(readOnly = true)
    public GetStoreReviewListResponseDto getStoreReviewList(long storeId, Pageable pageable) {
        return GetStoreReviewListResponseDto.from(reviewFacade.getStoreReviewList(storeId, pageable));
    }

    @Transactional(readOnly = true)
    public GetMyReviewListResponseDto getUserReviewList(long userId, Pageable pageable) {
        return GetMyReviewListResponseDto.from(reviewFacade.getUserReviewList(userId, pageable));
    }

    @Transactional
    public void deleteReview(long userId, long orderId, long reviewId) {
        ReviewQueryDto reviewQueryDto = reviewFacade.getReview(orderId, reviewId);

        if (reviewQueryDto.getReviewerId() != userId) {
            throw new NotFoundException(NOT_FOUND_REVIEW);
        }

        Review review = reviewQueryDto.getReview();

        Store store = reviewFacade.getStore(reviewQueryDto.getStoreId());
        store.deletedReview(review.getRating(), 1);
        review.delete();

        reviewFacade.saveReview(store, review);
    }
}
