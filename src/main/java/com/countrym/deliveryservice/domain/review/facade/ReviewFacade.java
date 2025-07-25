package com.countrym.deliveryservice.domain.review.facade;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderReviewQueryDto;
import com.countrym.deliveryservice.domain.order.repository.OrderRepository;
import com.countrym.deliveryservice.domain.order.repository.OrderedMenuRepository;
import com.countrym.deliveryservice.domain.review.dto.projection.ReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.StoreReviewQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.UserReviewQueryDto;
import com.countrym.deliveryservice.domain.review.entity.Review;
import com.countrym.deliveryservice.domain.review.repository.ReviewRepository;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final OrderedMenuRepository orderedMenuRepository;
    private final ReviewRepository reviewRepository;

    public Store getStore(long storeId) {
        return storeRepository.findByStoreId(storeId);
    }

    public OrderReviewQueryDto getOrder(long orderId) {
        return orderRepository.findByOrderIdAndReviewIsNull(orderId);
    }

    public void saveReview(Store store, Review review) {
        storeRepository.save(store);
        reviewRepository.save(review);
    }

    public ReviewQueryDto getReview(long orderId, long reviewId) {
        return reviewRepository.findByOrderIdAndReviewId(orderId, reviewId);
    }

    public List<StoreReviewQueryDto> getStoreReviewList(long storeId, Pageable pageable) {
        List<StoreReviewQueryDto> storeReviewList = reviewRepository.findByStoreIdAndPaging(storeId, pageable);

        List<Long> orderIdList = storeReviewList.stream().map(StoreReviewQueryDto::getOrderId).toList();
        List<OrderedMenuSimpleQueryDto> orderedMenuList = orderedMenuRepository.findAllByOrderIdList(orderIdList);

        Map<Long, List<OrderedMenuSimpleQueryDto>> orderedMenuMap
                = orderedMenuList.stream()
                .collect(Collectors.groupingBy(OrderedMenuSimpleQueryDto::getOrderId));

        storeReviewList.forEach(review ->
                review.setOrderedMenuList(orderedMenuMap.getOrDefault(review.getOrderId(), Collections.emptyList())));

        return storeReviewList;
    }

    public List<UserReviewQueryDto> getUserReviewList(long userId, Pageable pageable) {
        return reviewRepository.findByUserIdAndPaging(userId, pageable);
    }
}
