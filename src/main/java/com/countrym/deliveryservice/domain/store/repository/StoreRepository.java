package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_STORE;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByName(String name);

    default Store findByStoreId(Long storeId) {
        return findById(storeId).orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
    }
}