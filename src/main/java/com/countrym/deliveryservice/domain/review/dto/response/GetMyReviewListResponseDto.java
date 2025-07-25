package com.countrym.deliveryservice.domain.review.dto.response;

import com.countrym.deliveryservice.domain.review.dto.projection.UserReviewQueryDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.countrym.deliveryservice.common.util.TimeFormatter.convertToString;

@Getter
@RequiredArgsConstructor
public class GetMyReviewListResponseDto {
    private final List<GetUserReviewResponseDto> myReviewList;

    public static GetMyReviewListResponseDto from(List<UserReviewQueryDto> userReviewQueryDtoList) {
        return new GetMyReviewListResponseDto(
                userReviewQueryDtoList.stream().map(GetUserReviewResponseDto::from).toList()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class GetUserReviewResponseDto {
        private final long reviewId;
        private final long orderId;
        private final String storeName;
        private final double rating;
        private final String details;
        private final String reviewedAt;

        public static GetUserReviewResponseDto from(UserReviewQueryDto userReviewQueryDto) {
            return new GetUserReviewResponseDto(
                    userReviewQueryDto.getReviewId(),
                    userReviewQueryDto.getOrderId(),
                    userReviewQueryDto.getStoreName(),
                    userReviewQueryDto.getRating(),
                    userReviewQueryDto.getDetails(),
                    convertToString(userReviewQueryDto.getReviewedAt())
            );
        }
    }
}
