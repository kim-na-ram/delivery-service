package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreQueryRepository {
    List<GetStoreListResponseDto> findAllByTypeAndNameByPaging(StoreType type, String name, Pageable pageable);
}
