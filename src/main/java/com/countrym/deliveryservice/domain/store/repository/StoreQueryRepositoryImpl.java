package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.domain.menu.dto.projection.QMenuQueryDto;
import com.countrym.deliveryservice.domain.store.dto.projection.QStoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.countrym.deliveryservice.domain.menu.entity.QMenu.menu;
import static com.countrym.deliveryservice.domain.store.entity.QStore.store;
import static com.countrym.deliveryservice.domain.store.enums.StoreSortType.HIGH_AVERAGE_RATING;
import static com.countrym.deliveryservice.domain.store.enums.StoreSortType.MANY_ORDERS;
import static com.countrym.deliveryservice.domain.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Slf4j
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsOpenedStoreByOwnerId(long ownerId) {
        return jpaQueryFactory
                .from(store)
                .join(store.owner, user)
                .where(
                        user.id.eq(ownerId),
                        store.deletedAt.isNull()
                )
                .fetchOne() != null;
    }

    @Override
    public Optional<StoreMenuListDto> findByIdWithMenuList(long storeId) {
        Map<Long, StoreMenuListDto> result = jpaQueryFactory
                .from(store)
                .leftJoin(store.menuList, menu)
                .where(
                        store.id.eq(storeId),
                        store.deletedAt.isNull(),
                        menu.deletedAt.isNull()
                )
                .transform(
                        groupBy(store.id)
                                .as(
                                        new QStoreMenuListDto(
                                                store.name,
                                                store.thumbnailUrl,
                                                store.details,
                                                store.address1,
                                                store.address2,
                                                store.openAt,
                                                store.closedAt,
                                                store.minOrderPrice,
                                                store.totalOrderCount,
                                                store.averageRating,
                                                store.totalReviewCount,
                                                list(
                                                        new QMenuQueryDto(
                                                                menu.id,
                                                                menu.name,
                                                                menu.thumbnailUrl,
                                                                menu.details,
                                                                menu.price
                                                        )
                                                )
                                        )
                                )
                );

        if (result.isEmpty()) return Optional.empty();
        else return Optional.of(result.get(result.keySet().iterator().next()));
    }

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
                                store.totalOrderCount,
                                store.averageRating,
                                store.totalReviewCount
                        )
                )
                .from(store)
                .where(
                        store.deletedAt.isNull(),
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
                NumberPath<Integer> ordersPath = path.getNumber("totalOrderCount", Integer.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, ordersPath));
                NumberPath<Double> averageRatingPath = path.getNumber("averageRating", Double.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, averageRatingPath));
            } else if (property.equals(HIGH_AVERAGE_RATING.name())) {
                NumberPath<Double> averageRatingPath = path.getNumber("averageRating", Double.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, averageRatingPath));
                NumberPath<Integer> totalReviewCountPath = path.getNumber("totalReviewCount", Integer.class);
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, totalReviewCountPath));
            } else {
                orderSpecifiers.add(new OrderSpecifier(order, path.get(property)));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
}
