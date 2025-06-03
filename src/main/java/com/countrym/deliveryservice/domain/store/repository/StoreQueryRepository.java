package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreQueryRepository {
    Optional<StoreMenuListDto> findByIdWithMenuList(long storeId);
    List<GetStoreListResponseDto> findAllByTypeAndNameByPaging(StoreType type, String name, Pageable pageable);
}
