package com.countrym.deliveryservice.domain.review.service;

import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderReviewQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.request.RegisterReviewRequestDto;
import com.countrym.deliveryservice.domain.review.facade.ReviewFacade;
import com.countrym.deliveryservice.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;
import static com.countrym.deliveryservice.domain.data.review.ReviewMockData.*;
import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStore;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewFacade reviewFacade;

    @InjectMocks
    private ReviewService reviewService;

    @Nested
    @DisplayName("리뷰 등록")
    class RegisterReview {
        @Test
        @DisplayName("주문한 사용자와 리뷰 작성 사용자가 다르면 리뷰 등록 실패")
        public void registerReview_notUserOwnOrder_failure() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            RegisterReviewRequestDto registerReviewRequestDto = getRegisterReviewRequestDto();

            OrderReviewQueryDto orderReviewQueryDto = getOrderReviewQueryDto();
            ReflectionTestUtils.setField(orderReviewQueryDto, "orderUserId", 2L);

            given(reviewFacade.getOrder(anyLong())).willReturn(orderReviewQueryDto);

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> reviewService.registerReview(reviewerId, orderId, registerReviewRequestDto));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 상태가 '배달 완료'가 아니라면 리뷰 등록 실패")
        public void registerReview_orderIsNotCompleted_failure() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            RegisterReviewRequestDto registerReviewRequestDto = getRegisterReviewRequestDto();

            OrderReviewQueryDto orderReviewQueryDto = getOrderReviewQueryDto();

            given(reviewFacade.getOrder(anyLong())).willReturn(orderReviewQueryDto);

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> reviewService.registerReview(reviewerId, orderId, registerReviewRequestDto));

            // then
            assertEquals(INVALID_REVIEW_NOT_COMPLETED_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문한지 3일이 지난 주문에 대해서는 리뷰 등록 실패")
        public void registerReview_orderedAtThreeDaysAgo_failure() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            RegisterReviewRequestDto registerReviewRequestDto = getRegisterReviewRequestDto();

            OrderReviewQueryDto orderReviewQueryDto = getOrderReviewQueryDto();
            ReflectionTestUtils.setField(orderReviewQueryDto.getOrder(), "status", OrderStatus.DELIVERY_COMPLETED);
            ReflectionTestUtils.setField(orderReviewQueryDto.getOrder(), "orderedAt", LocalDateTime.now().minusDays(10));

            given(reviewFacade.getOrder(anyLong())).willReturn(orderReviewQueryDto);

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> reviewService.registerReview(reviewerId, orderId, registerReviewRequestDto));

            // then
            assertEquals(INVALID_REVIEW_ORDER_DATE_OVER_THREE_DAYS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("성공")
        public void registerReview_success() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            RegisterReviewRequestDto registerReviewRequestDto = getRegisterReviewRequestDto();

            OrderReviewQueryDto orderReviewQueryDto = getOrderReviewQueryDto();
            ReflectionTestUtils.setField(orderReviewQueryDto.getOrder(), "status", OrderStatus.DELIVERY_COMPLETED);
            ReflectionTestUtils.setField(orderReviewQueryDto.getOrder(), "orderedAt", LocalDateTime.now().minusDays(2));

            Store store = getStore(getOwnerInfo());

            given(reviewFacade.getOrder(anyLong())).willReturn(orderReviewQueryDto);
            given(reviewFacade.getStore(anyLong())).willReturn(store);

            double prevTotalRating = store.getTotalRating();
            int prevTotalReviewCount = store.getTotalReviewCount();

            // when, then
            assertDoesNotThrow(() -> reviewService.registerReview(reviewerId, orderId, registerReviewRequestDto));
            assertEquals(prevTotalRating + registerReviewRequestDto.getRating(), store.getTotalRating());
            assertEquals(prevTotalReviewCount + 1, store.getTotalReviewCount());
        }
    }

    @Nested
    @DisplayName("리뷰 삭제")
    class DeleteReview {
        @Test
        @DisplayName("리뷰를 작성한 사용자가 아니라면 리뷰 삭제 실패")
        public void deleteReview_notUserOwnReview_failure() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            long reviewId = 1L;
            ReviewQueryDto reviewQueryDto = getReviewQueryDto();
            ReflectionTestUtils.setField(reviewQueryDto, "reviewerId", 2L);

            given(reviewFacade.getReview(anyLong(), anyLong())).willReturn(reviewQueryDto);

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> reviewService.deleteReview(reviewerId, orderId, reviewId));

            // then
            assertEquals(NOT_FOUND_REVIEW.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("성공")
        void deleteReview_success() {
            // given
            long reviewerId = 1L;
            long orderId = 1L;
            long reviewId = 1L;

            ReviewQueryDto reviewQueryDto = getReviewQueryDto();
            Store store = getStore(getOwnerInfo());

            given(reviewFacade.getReview(anyLong(), anyLong())).willReturn(reviewQueryDto);
            given(reviewFacade.getStore(anyLong())).willReturn(store);

            double prevTotalRating = store.getTotalRating();
            int prevTotalReviewCount = store.getTotalReviewCount();

            // when, then
            assertDoesNotThrow(() -> reviewService.deleteReview(reviewerId, orderId, reviewId));
            assertEquals(prevTotalRating - getRegisterReviewRequestDto().getRating(), store.getTotalRating());
            assertEquals(prevTotalReviewCount - 1, store.getTotalReviewCount());
        }
    }
}