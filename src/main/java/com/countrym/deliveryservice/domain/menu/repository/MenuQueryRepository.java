package com.countrym.deliveryservice.domain.menu.repository;

import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.exception.ResponseCode;
import com.countrym.deliveryservice.domain.menu.dto.projection.StoreOwnerMenuDto;

import java.util.Optional;

public interface MenuQueryRepository {
    Optional<StoreOwnerMenuDto> findByIdAndStoreId(long menuId, long storeId);

    default StoreOwnerMenuDto findByMenuIdAndStoreId(long menuId, long storeId) {
        return findByIdAndStoreId(menuId, storeId)
                .orElseThrow(() -> new InvalidParameterException(ResponseCode.NOT_FOUND_STORE_MENU));
    }
}
