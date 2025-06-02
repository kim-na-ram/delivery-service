package com.countrym.deliveryservice.domain.menu.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.domain.menu.dto.projection.StoreOwnerMenuDto;
import com.countrym.deliveryservice.domain.menu.dto.request.ModifyMenuRequestDto;
import com.countrym.deliveryservice.domain.menu.dto.request.RegisterMenuRequestDto;
import com.countrym.deliveryservice.domain.menu.dto.response.GetMenuResponseDto;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import com.countrym.deliveryservice.domain.menu.repository.MenuRepository;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.countrym.deliveryservice.common.exception.ResponseCode.INVALID_STORE_OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public GetMenuResponseDto registerMenu(long storeId, UserInfo userInfo, RegisterMenuRequestDto registerMenuRequestDto) {
        Store store = storeRepository.findByStoreId(storeId);
        if (store.getOwner().getId() != userInfo.getId())
            throw new InvalidParameterException(INVALID_STORE_OWNER);

        Menu menu = menuRepository.save(Menu.from(store, registerMenuRequestDto));

        return GetMenuResponseDto.from(menu);
    }

    @Transactional
    public GetMenuResponseDto modifyMenu(long storeId, long menuId, UserInfo userInfo, ModifyMenuRequestDto modifyMenuRequestDto) {
        StoreOwnerMenuDto storeOwnerMenuDto = menuRepository.findByMenuIdAndStoreId(menuId, storeId);

        if (storeOwnerMenuDto.getOwnerId() != userInfo.getId())
            throw new InvalidParameterException(INVALID_STORE_OWNER);

        Menu menu = Menu.from(storeOwnerMenuDto);
        menu.modify(modifyMenuRequestDto);
        menuRepository.save(menu);

        return GetMenuResponseDto.from(menu);
    }

    @Transactional
    public void deleteMenu(long storeId, long menuId, UserInfo userInfo) {
        StoreOwnerMenuDto storeOwnerMenuDto = menuRepository.findByMenuIdAndStoreId(menuId, storeId);
        if (storeOwnerMenuDto.getOwnerId() != userInfo.getId())
            throw new InvalidParameterException(INVALID_STORE_OWNER);

        Menu menu = Menu.from(storeOwnerMenuDto);
        menu.delete();
        menuRepository.save(menu);
    }
}
