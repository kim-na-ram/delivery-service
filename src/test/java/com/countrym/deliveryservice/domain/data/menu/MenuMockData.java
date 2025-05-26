package com.countrym.deliveryservice.domain.data.menu;

import com.countrym.deliveryservice.domain.menu.dto.request.RegisterMenuRequestDto;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStoreWithoutMenuList;

public class MenuMockData {
    public static RegisterMenuRequestDto getRegisterMenuRequestDto() {
        return new RegisterMenuRequestDto(
                "menu name",
                "thumbnail",
                "menu details",
                12000
        );
    }

    public static RegisterMenuRequestDto getRegisterMenuRequestDto(int number) {
        return new RegisterMenuRequestDto(
                "menu name " + number,
                "thumbnail",
                "menu details",
                12000
        );
    }

    public static Menu getMenu() {
        return Menu.from(getStoreWithoutMenuList(), getRegisterMenuRequestDto());
    }

    public static Menu getMenu(int number) {
        Menu menu = Menu.from(getStoreWithoutMenuList(), getRegisterMenuRequestDto(number));
        ReflectionTestUtils.setField(menu, "id", Integer.valueOf(number).longValue());
        return menu;
    }

    public static List<Menu> getMenuList(int number) {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            menuList.add(getMenu(i));
        }
        return menuList;
    }
}
