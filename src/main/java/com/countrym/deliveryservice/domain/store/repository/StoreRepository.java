package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_STORE;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryRepository {
    boolean existsByName(String name);

    default Store findByStoreId(long storeId) {
        return findById(storeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
    }

    default StoreMenuListDto findByStoreIdWithMenuList(long storeId) {
        return findByIdWithMenuList(storeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
    }
}