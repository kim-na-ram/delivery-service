package com.countrym.deliveryservice.domain.store.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.DuplicateElementException;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.ModifyStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.RegisterStoreResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.store.facade.StoreFacade;
import com.countrym.deliveryservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreFacade storeFacade;

    @Transactional
    public RegisterStoreResponseDto registerStore(UserInfo userInfo, RegisterStoreRequestDto registerStoreRequestDto) {
        validateStoreName(registerStoreRequestDto.getName());

        Store store = storeFacade.saveStore(Store.from(User.from(userInfo), registerStoreRequestDto));

        return RegisterStoreResponseDto.from(store);
    }

    @Transactional(readOnly = true)
    public GetStoreResponseDto getStore(long storeId) {
        StoreMenuListDto storeMenuListDto = storeFacade.getStoreWithMenuList(storeId);
        return GetStoreResponseDto.from(storeMenuListDto);
    }

    @Transactional(readOnly = true)
    public List<GetStoreListResponseDto> getStoreList(String type, String name, Pageable pageable) {
        return storeFacade.getStoreByPaging(
                StringUtils.hasText(type) ? StoreType.of(type) : null,
                name,
                pageable
        );
    }

    @Transactional
    public ModifyStoreResponseDto modifyStore(long storeId, UserInfo userInfo, ModifyStoreRequestDto modifyStoreRequestDto) {
        Store store = storeFacade.getStore(storeId);
        validateUserOwnStore(userInfo.getId(), store);

        if (StringUtils.hasText(modifyStoreRequestDto.getName())) {
            if (!store.getName().equals(modifyStoreRequestDto.getName()))
                validateStoreName(modifyStoreRequestDto.getName());
        }

        store.modify(modifyStoreRequestDto);
        storeFacade.saveStore(store);

        return ModifyStoreResponseDto.from(store);
    }

    private void validateUserOwnStore(long userId, Store store) {
        if (store.getOwner().getId() != userId)
            throw new InvalidParameterException(INVALID_STORE_OWNER);
    }

    private void validateStoreName(String name) {
        if (storeFacade.existsByName(name)) {
            throw new DuplicateElementException(ALREADY_EXISTS_STORE_ERROR);
        }
    }

    @Transactional
    public void closureStore(long storeId, UserInfo userInfo) {
        Store store = storeFacade.getStore(storeId);
        validateUserOwnStore(userInfo.getId(), store);

        // 주문이 있다면 폐업 불가능
        if (storeFacade.existUncompletedOrderInStore(storeId)) {
            throw new InvalidParameterException(INVALID_UNCOMPLETED_ORDER_IN_STORE);
        }

        store.closure();
        storeFacade.saveStore(store);
    }
}