package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.domain.menu.dto.projection.QOrderedMenuQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.*;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.countrym.deliveryservice.domain.menu.entity.QMenu.menu;
import static com.countrym.deliveryservice.domain.order.entity.QOrder.order;
import static com.countrym.deliveryservice.domain.order.entity.QOrderedMenu.orderedMenu;
import static com.countrym.deliveryservice.domain.store.entity.QStore.store;
import static com.countrym.deliveryservice.domain.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public boolean existsByUserIdAndStatusNotEquals(long userId, OrderStatus status) {
        return jpaQueryFactory.
                from(order)
                .join(order.user, user)
                .where(
                        user.id.eq(userId),
                        order.status.ne(status)
                )
                .fetchFirst() != null;
    }

    public boolean existsByStoreIdAndStatusNotEquals(long storeId, OrderStatus status) {
        return jpaQueryFactory.
                from(order)
                .join(order.store, store)
                .where(
                        store.id.eq(storeId),
                        order.status.ne(status)
                )
                .fetchFirst() != null;
    }

    public Optional<OrderDetailQueryDto> findByIdWithMenuList(long orderId) {
        Map<Long, OrderDetailQueryDto> result = jpaQueryFactory
                .from(order)
                .join(order.store, store)
                .join(order.orderedMenuList, orderedMenu)
                .join(orderedMenu.menu, menu)
                .where(order.id.eq(orderId))
                .transform(
                        groupBy(order.id)
                                .as(
                                        new QOrderDetailQueryDto(
                                                order.id,
                                                order.user.id,
                                                order.user.nickname,
                                                order.store.id,
                                                store.name,
                                                store.owner.id,
                                                list(
                                                        new QOrderedMenuQueryDto(
                                                                orderedMenu.menu.id,
                                                                menu.name,
                                                                menu.price,
                                                                orderedMenu.quantity
                                                        )
                                                ),
                                                order.price,
                                                order.paymentMethod,
                                                order.status,
                                                order.orderedAt,
                                                order.canceledAt
                                        )
                                )
                );

        if (result.isEmpty()) return Optional.empty();
        else return Optional.of(result.get(result.keySet().iterator().next()));
    }

    public Optional<List<OrderSimpleQueryDto>> findAllByUserIdAndPaging(long userId, OrderStatus orderStatus, Pageable pageable) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                new QOrderSimpleQueryDto(
                                        order.id,
                                        order.store.id,
                                        order.store.name,
                                        order.price,
                                        order.status,
                                        order.orderedAt,
                                        order.canceledAt,
                                        // TODO review 작성(존재)유무
                                        Expressions.asBoolean(false)
                                )
                        )
                        .from(order)
                        .join(order.store, store)
                        .join(order.user, user)
                        .where(
                                user.id.eq(userId),
                                order.orderedAt.after(LocalDateTime.now().minusMonths(3)),
                                orderStatusEquals(orderStatus)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(order.orderedAt.desc())
                        .fetch()
        );
    }

    public Optional<List<OrderSimpleQueryDto>> findAllByOwnerIdAndPaging(long ownerId, OrderStatus orderStatus, Pageable pageable) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                new QOrderSimpleQueryDto(
                                        order.id,
                                        order.store.id,
                                        order.store.name,
                                        order.price,
                                        order.status,
                                        order.orderedAt,
                                        order.canceledAt,
                                        // TODO review 작성(존재)유무
                                        Expressions.asBoolean(false)
                                )
                        )
                        .from(order)
                        .join(order.store, store)
                        .join(store.owner, user)
                        .where(
                                user.id.eq(ownerId),
                                order.orderedAt.after(LocalDateTime.now().minusMonths(3)),
                                orderStatusEquals(orderStatus)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(order.orderedAt.desc())
                        .fetch()
        );
    }

    public Optional<List<StoreOrderQueryDto>> findAllByStoreIdAndPaging(long storeId, OrderStatus status, Pageable pageable) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                new QStoreOrderQueryDto(
                                        order.id,
                                        store.owner.id,
                                        order.price,
                                        order.status,
                                        order.orderedAt,
                                        order.canceledAt,
                                        // TODO review 작성(존재)유무
                                        Expressions.asBoolean(false)
                                )
                        )
                        .from(order)
                        .join(order.store, store)
                        .join(store.owner, user)
                        .where(
                                order.store.id.eq(storeId),
                                orderStatusEquals(status)
                        )
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch());
    }

    public Optional<OrderQueryDto> findById(long orderId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(new QOrderQueryDto(
                                store.id,
                                store.owner.id,
                                order.user.id,
                                order
                        ))
                        .from(order)
                        .join(order.store, store)
                        .where(order.id.eq(orderId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }

    private BooleanExpression orderStatusEquals(OrderStatus orderStatus) {
        return orderStatus == null ? null : order.status.eq(orderStatus);
    }
}
