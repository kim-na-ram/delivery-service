package com.countrym.deliveryservice.domain.review.controller;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.response.SuccessResponse;
import com.countrym.deliveryservice.domain.review.dto.request.RegisterReviewRequestDto;
import com.countrym.deliveryservice.domain.review.dto.response.GetMyReviewListResponseDto;
import com.countrym.deliveryservice.domain.review.dto.response.GetStoreReviewListResponseDto;
import com.countrym.deliveryservice.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<SuccessResponse<Void>> registerReview(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable long orderId,
            @Valid @RequestBody RegisterReviewRequestDto registerReviewRequestDto
    ) {
        reviewService.registerReview(userInfo.getId(), orderId, registerReviewRequestDto);
        return ResponseEntity.ok(SuccessResponse.of());
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<SuccessResponse<GetStoreReviewListResponseDto>> getStoreReviewList(
            @PathVariable long storeId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        GetStoreReviewListResponseDto getStoreReviewListResponseDto = reviewService.getStoreReviewList(storeId, pageable);
        return ResponseEntity.ok(SuccessResponse.of(getStoreReviewListResponseDto));
    }

    @GetMapping("/users/reviews")
    public ResponseEntity<SuccessResponse<GetMyReviewListResponseDto>> getMyReviewList(
            @AuthenticationPrincipal UserInfo userInfo,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        GetMyReviewListResponseDto getMyReviewListResponseDto = reviewService.getUserReviewList(userInfo.getId(), pageable);
        return ResponseEntity.ok(SuccessResponse.of(getMyReviewListResponseDto));
    }

    @DeleteMapping("/orders/{orderId}/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReview(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable long orderId,
            @PathVariable long reviewId
    ) {
        reviewService.deleteReview(userInfo.getId(), orderId, reviewId);
        return ResponseEntity.ok(SuccessResponse.of());
    }
}
