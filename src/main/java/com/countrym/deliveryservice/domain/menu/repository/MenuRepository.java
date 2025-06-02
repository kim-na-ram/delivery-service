package com.countrym.deliveryservice.domain.menu.repository;

import com.countrym.deliveryservice.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuQueryRepository {
}
