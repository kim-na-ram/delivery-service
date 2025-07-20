package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.menu.dto.projection.QOrderedMenuSimpleQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.countrym.deliveryservice.domain.menu.entity.QMenu.menu;
import static com.countrym.deliveryservice.domain.order.entity.QOrder.order;
import static com.countrym.deliveryservice.domain.order.entity.QOrderedMenu.orderedMenu;

@RequiredArgsConstructor
public class OrderedMenuQueryRepositoryImpl implements OrderedMenuQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderedMenuSimpleQueryDto> findAllByOrderIdList(List<Long> orderIdList) {
        return jpaQueryFactory
                .select(
                        new QOrderedMenuSimpleQueryDto(
                                order.id,
                                orderedMenu.menu.id,
                                menu.name,
                                orderedMenu.quantity
                        )
                )
                .from(orderedMenu)
                .join(orderedMenu.order, order)
                .join(orderedMenu.menu, menu)
                .where(order.id.in(orderIdList))
                .fetch();
    }
}
