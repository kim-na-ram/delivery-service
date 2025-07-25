package com.countrym.deliveryservice.domain.store.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStore;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StoreTest {
    private Store store;

    @Test
    @DisplayName("새로운 리뷰가 등록되면 평균 평점을 다시 계산")
    public void calculateAverageRating_registeredReview() {
        Store store = getStore(getUserInfo());
        ReflectionTestUtils.setField(store, "totalRating", 23.5);
        ReflectionTestUtils.setField(store, "totalReviewCount", 6);

        store.registeredReview(4.0, 1);

        assertEquals(3.92, store.getAverageRating());
    }

    @Test
    @DisplayName("리뷰가 삭제되면 평균 평점을 다시 계산")
    public void calculateAverageRating_deletedReview() {
        Store store = getStore(getUserInfo());
        ReflectionTestUtils.setField(store, "totalRating", 27.5);
        ReflectionTestUtils.setField(store, "totalReviewCount", 7);

        store.deletedReview(4.0, 1);

        assertEquals(3.91, store.getAverageRating());
    }
}