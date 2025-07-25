package com.countrym.deliveryservice.domain.data.review;

import com.countrym.deliveryservice.domain.order.dto.projection.OrderReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.request.RegisterReviewRequestDto;
import com.countrym.deliveryservice.domain.review.entity.Review;
import com.countrym.deliveryservice.domain.user.entity.User;

import static com.countrym.deliveryservice.domain.data.order.OrderMockData.getOrder;
import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStore;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserInfo;

public class ReviewMockData {
    public static RegisterReviewRequestDto getRegisterReviewRequestDto() {
        return new RegisterReviewRequestDto(
                5.0,
                "너무 맛있어요! 다음에 또 시켜먹을 것 같습니다 ㅎㅅㅎ"
        );
    }

    public static OrderReviewQueryDto getOrderReviewQueryDto() {
        return new OrderReviewQueryDto(
                1L,
                1L,
                getOrder()
        );
    }

    public static Review getReview() {
        return Review.from(
                User.from(getUserInfo()),
                getOrder(),
                getStore(getOwnerInfo()),
                getRegisterReviewRequestDto()
        );
    }

    public static ReviewQueryDto getReviewQueryDto() {
        return new ReviewQueryDto(
                1L,
                1L,
                getReview()
        );
    }
}
