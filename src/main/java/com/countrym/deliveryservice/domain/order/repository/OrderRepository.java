package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.common.exception.ResponseCode;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderDetailQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
//    default Order findByOrderId(long orderId) {
//        return findById(orderId)
//                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER));
//    }

    default OrderDetailQueryDto findByOrderIdWithMenuList(long orderId) {
        return findByIdWithMenuList(orderId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER));
    }

    default OrderQueryDto findByOrderId(long orderId) {
        return findById(orderId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER));
    }

    default List<OrderSimpleQueryDto> findListByUserIdAndPageable(long userId, OrderStatus orderStatus, Pageable pageable) {
        return findAllByUserIdAndPaging(userId, orderStatus, pageable)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER_LIST_IN_THREE_MONTH));
    }

    default List<OrderSimpleQueryDto> findListByOwnerIdAndPageable(long ownerId, OrderStatus orderStatus, Pageable pageable) {
        return findAllByOwnerIdAndPaging(ownerId, orderStatus, pageable)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER_LIST_IN_THREE_MONTH));
    }

    default List<StoreOrderQueryDto> findListByStoreIdAndPageable(long storeId, OrderStatus orderStatus, Pageable pageable) {
        return findAllByStoreIdAndPaging(storeId, orderStatus, pageable)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ORDER_LIST_IN_THREE_MONTH));
    }
}
