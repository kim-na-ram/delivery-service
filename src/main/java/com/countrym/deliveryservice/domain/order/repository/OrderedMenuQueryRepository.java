package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;

import java.util.List;

interface OrderedMenuQueryRepository {
    List<OrderedMenuSimpleQueryDto> findAllByOrderIdList(List<Long> orderIdList);
}
