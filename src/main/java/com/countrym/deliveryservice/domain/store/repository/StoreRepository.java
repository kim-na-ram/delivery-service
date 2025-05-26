package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_STORE;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreQueryRepository {
    boolean existsByName(String name);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH Menu m ON s.id = m.store.id WHERE s.id = :storeId")
    Optional<Store> findById(@Param("storeId") long id);

    default Store findByStoreId(long storeId) {
        return findById(storeId).orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
    }
}