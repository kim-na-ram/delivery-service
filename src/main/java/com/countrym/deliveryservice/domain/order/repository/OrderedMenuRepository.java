package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.domain.order.entity.OrderedMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuRepository extends JpaRepository<OrderedMenu, Long>, OrderedMenuQueryRepository {
}
