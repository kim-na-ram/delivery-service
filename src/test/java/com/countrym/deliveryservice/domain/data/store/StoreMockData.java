package com.countrym.deliveryservice.domain.data.store;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.user.entity.User;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;

import static com.countrym.deliveryservice.domain.data.menu.MenuMockData.getMenu;
import static com.countrym.deliveryservice.domain.data.menu.MenuMockData.getMenuList;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;

public class StoreMockData {
    public static RegisterStoreRequestDto getRegisterStoreRequestDto() {
        return new RegisterStoreRequestDto(
                "name",
                "thumbnailUrl",
                "details",
                "address1",
                "address2",
                StoreType.CAFE.name(),
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                10000
        );
    }

    public static ModifyStoreRequestDto getModifyStoreRequestDto() {
        return new ModifyStoreRequestDto(
                "modify name",
                "thumbnailUrl",
                "details",
                "address1",
                "address2",
                StoreType.CAFE.name(),
                LocalTime.of(11, 0),
                LocalTime.of(22, 0),
                10000
        );
    }

    public static Store getStore(UserInfo userInfo, RegisterStoreRequestDto registerStoreRequestDto) {
        Store store = Store.from(User.from(userInfo), registerStoreRequestDto);
        ReflectionTestUtils.setField(store, "id", 1L);

        return store;
    }

    public static Store getStore(UserInfo userInfo) {
        Store store = Store.from(User.from(userInfo), getRegisterStoreRequestDto());
        ReflectionTestUtils.setField(store, "id", 1L);

        return store;
    }

    public static Store getStoreWithoutMenuList() {
        Store store = Store.from(User.from(getOwnerInfo()), getRegisterStoreRequestDto());
        ReflectionTestUtils.setField(store, "id", 1L);

        return store;
    }

    public static Store getStoreWithMenuList() {
        Store store = Store.from(User.from(getOwnerInfo()), getRegisterStoreRequestDto());
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "menuList", getMenuList(5));

        return store;
    }
}
