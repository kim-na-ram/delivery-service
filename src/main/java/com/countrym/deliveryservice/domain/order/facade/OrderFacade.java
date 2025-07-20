package com.countrym.deliveryservice.domain.order.facade;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderDetailQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.repository.OrderRepository;
import com.countrym.deliveryservice.domain.order.repository.OrderedMenuRepository;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import com.countrym.deliveryservice.domain.user.enums.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final OrderedMenuRepository orderedMenuRepository;

    public Store getStore(long storeId) {
        return storeRepository.findByStoreId(storeId);
    }

    public OrderQueryDto getOrder(long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public OrderDetailQueryDto getOrderDetail(long orderId) {
        return orderRepository.findByOrderIdWithMenuList(orderId);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public void saveOrderAndStore(Order order, Store store) {
        orderRepository.save(order);
        storeRepository.save(store);
    }

    public List<OrderSimpleQueryDto> getOrderList(long userId, Authority authority, OrderStatus status, Pageable pageable) {
        List<OrderSimpleQueryDto> orderList;
        if (authority.isUser())
            orderList = orderRepository.findListByUserIdAndPageable(userId, status, pageable);
        else
            orderList = orderRepository.findListByOwnerIdAndPageable(userId, status, pageable);

        List<Long> orderIdList = orderList.stream().map(OrderSimpleQueryDto::getOrderId).toList();
        List<OrderedMenuSimpleQueryDto> orderedMenuList = orderedMenuRepository.findAllByOrderIdList(orderIdList);

        Map<Long, List<OrderedMenuSimpleQueryDto>> orderedMenuMap
                = orderedMenuList.stream()
                .collect(Collectors.groupingBy(OrderedMenuSimpleQueryDto::getOrderId));

        orderList.forEach(order ->
                order.setOrderedMenuList(orderedMenuMap.getOrDefault(order.getOrderId(), Collections.emptyList())));

        return orderList;
    }

    public List<StoreOrderQueryDto> getStoreOrderList(long storeId, OrderStatus status, Pageable pageable) {
        List<StoreOrderQueryDto> storeOrderList = orderRepository.findListByStoreIdAndPageable(storeId, status, pageable);

        List<Long> orderIdList = storeOrderList.stream().map(StoreOrderQueryDto::getOrderId).toList();
        List<OrderedMenuSimpleQueryDto> orderedMenuList = orderedMenuRepository.findAllByOrderIdList(orderIdList);

        Map<Long, List<OrderedMenuSimpleQueryDto>> orderedMenuMap
                = orderedMenuList.stream()
                .collect(Collectors.groupingBy(OrderedMenuSimpleQueryDto::getOrderId));

        storeOrderList.forEach(storeOrder ->
                storeOrder.setOrderedMenuList(orderedMenuMap.getOrDefault(storeOrder.getOrderId(), Collections.emptyList())));

        return storeOrderList;
    }
}
