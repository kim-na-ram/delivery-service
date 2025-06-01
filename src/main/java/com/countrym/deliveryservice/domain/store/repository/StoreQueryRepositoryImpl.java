package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.QGetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.countrym.deliveryservice.domain.store.entity.QStore.store;
import static com.countrym.deliveryservice.domain.store.enums.StoreSortType.HIGH_AVERAGE_RATING;
import static com.countrym.deliveryservice.domain.store.enums.StoreSortType.MANY_ORDERS;

@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetStoreListResponseDto> findAllByTypeAndNameByPaging(StoreType type, String name, Pageable pageable) {
        return jpaQueryFactory.select(
                        new QGetStoreListResponseDto(
                                store.id,
                                store.name,
                                store.thumbnailUrl,
                                store.openAt,
                                store.closedAt,
                                store.minOrderPrice,
                                store.averageRating,
                                store.reviewAmount
                        )
                )
                .from(store)
                .where(
                        storeTypeEquals(type),
                        storeNameEquals(name)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortBy(pageable))
                .fetch();
    }

    private BooleanExpression storeTypeEquals(StoreType type) {
        return type == null ? null : store.type.eq(type);
    }

    private BooleanExpression storeNameEquals(String name) {
        return StringUtils.hasText(name) ? store.name.contains(name) : null;
    }

    private OrderSpecifier<?>[] sortBy(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order sort : pageable.getSort()) {
            Order order = sort.isAscending() ? Order.ASC : Order.DESC;
            String property = sort.getProperty();
            PathBuilder<Store> path = new PathBuilder<>(Store.class, "store");

            if (property.equals(MANY_ORDERS.name())) {
                // TODO 주문 많은 순 구현
                NumberPath<Integer> ordersPath = path.getNumber("", Integer.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, ordersPath));
                NumberPath<Double> averageRatingPath = path.getNumber("averageRating", Double.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, averageRatingPath));
            } else if (property.equals(HIGH_AVERAGE_RATING.name())) {
                NumberPath<Double> averageRatingPath = path.getNumber("averageRating", Double.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, averageRatingPath));
                NumberPath<Integer> reviewAmountPath = path.getNumber("reviewAmount", Integer.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, reviewAmountPath));
            } else {
                orderSpecifiers.add(new OrderSpecifier(order, path.get(property)));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
}
