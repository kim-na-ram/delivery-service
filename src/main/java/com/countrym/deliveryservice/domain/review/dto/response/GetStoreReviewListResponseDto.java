package com.countrym.deliveryservice.domain.review.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.review.dto.projection.StoreReviewQueryDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.countrym.deliveryservice.common.util.TimeFormatter.convertToString;

@Getter
@RequiredArgsConstructor
public class GetStoreReviewListResponseDto {
    private final List<GetStoreReviewResponseDto> storeReviewList;

    public static GetStoreReviewListResponseDto from(List<StoreReviewQueryDto> storeReviewQueryDtoList) {
        return new GetStoreReviewListResponseDto(
                storeReviewQueryDtoList.stream().map(GetStoreReviewResponseDto::from).toList()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class GetStoreReviewResponseDto {
        private final long reviewId;
        private final String reviewerNickname;
        private final List<String> orderedMenuList;
        private final double rating;
        private final String details;
        private final String reviewedAt;

        public static GetStoreReviewResponseDto from(StoreReviewQueryDto storeReviewQueryDto) {
            return new GetStoreReviewResponseDto(
                    storeReviewQueryDto.getReviewId(),
                    storeReviewQueryDto.getReviewerNickname(),
                    storeReviewQueryDto.getOrderedMenuList().stream().map(OrderedMenuSimpleQueryDto::getName).toList(),
                    storeReviewQueryDto.getRating(),
                    storeReviewQueryDto.getDetails(),
                    convertToString(storeReviewQueryDto.getReviewedAt())
            );
        }
    }
}
