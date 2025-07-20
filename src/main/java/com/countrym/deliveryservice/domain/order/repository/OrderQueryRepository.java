package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.domain.order.dto.projection.OrderDetailQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    boolean existsByUserIdAndStatusNotEquals(long userId, OrderStatus status);

    boolean existsByStoreIdAndStatusNotEquals(long ownerId, OrderStatus status);

    Optional<OrderDetailQueryDto> findByIdWithMenuList(long orderId);

    Optional<List<OrderSimpleQueryDto>> findAllByUserIdAndPaging(long userId, OrderStatus orderStatus, Pageable pageable);

    Optional<List<OrderSimpleQueryDto>> findAllByOwnerIdAndPaging(long ownerId, OrderStatus orderStatus, Pageable pageable);

    Optional<List<StoreOrderQueryDto>> findAllByStoreIdAndPaging(long storeId, OrderStatus orderStatus, Pageable pageable);

    Optional<OrderQueryDto> findById(long orderId);
}
