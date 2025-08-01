package com.countrym.deliveryservice.domain.data.store;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

import static com.countrym.deliveryservice.domain.data.menu.MenuMockData.getMenuDtoList;
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
                LocalTime.of(LocalTime.now().minusHours(2).getHour(), 0),
                LocalTime.of(LocalTime.now().plusHours(2).getHour(), 0),
                10000
        );
    }

    public static ModifyStoreRequestDto getModifyStoreRequestDto() {
        return new ModifyStoreRequestDto(
                "modify name",
                "thumbnailUrl",
                null,
                null,
                null,
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
        ReflectionTestUtils.setField(store, "menuList", getMenuList(null));

        return store;
    }

    public static StoreMenuListDto getStoreMenuListDto(int number) {
        return new StoreMenuListDto(
                "name",
                null,
                "details",
                "address1",
                "address2",
                LocalTime.of(10, 0),
                LocalTime.of(20, 0),
                10000,
                5,
                5.0,
                3,
                getMenuDtoList(number)
        );
    }
}
