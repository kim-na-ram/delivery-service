package com.countrym.deliveryservice.domain.review.repository;

import com.countrym.deliveryservice.domain.review.dto.projection.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.countrym.deliveryservice.domain.review.entity.QReview.review;
import static com.countrym.deliveryservice.domain.store.entity.QStore.store;
import static com.countrym.deliveryservice.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ReviewQueryDto> findByOrderIdAndId(long orderId, long reviewId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(new QReviewQueryDto(
                                review.reviewer.id,
                                review.store.id,
                                review
                        ))
                        .from(review)
                        .where(
                                review.order.id.eq(orderId),
                                review.id.eq(reviewId),
                                review.deletedAt.isNull()
                        )
                        .fetchFirst()
        );
    }

    @Override
    public List<StoreReviewQueryDto> findByStoreIdAndPaging(long storeId, Pageable pageable) {
        return jpaQueryFactory
                .select(new QStoreReviewQueryDto(
                        review.id,
                        review.order.id,
                        user.nickname,
                        review.rating,
                        review.details,
                        review.reviewedAt
                ))
                .from(review)
                .join(review.reviewer, user)
                .where(
                        review.store.id.eq(storeId),
                        review.deletedAt.isNull()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(review.reviewedAt.desc())
                .fetchJoin()
                .fetch();
    }

    @Override
    public List<UserReviewQueryDto> findByUserIdAndPaging(long userId, Pageable pageable) {
        return jpaQueryFactory
                .select(new QUserReviewQueryDto(
                        review.id,
                        review.order.id,
                        store.name,
                        review.rating,
                        review.details,
                        review.reviewedAt
                ))
                .from(review)
                .join(review.store, store)
                .where(
                        review.reviewer.id.eq(userId),
                        review.deletedAt.isNull()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(review.reviewedAt.desc())
                .fetch();
    }
}
