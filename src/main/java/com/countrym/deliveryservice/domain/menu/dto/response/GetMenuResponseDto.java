package com.countrym.deliveryservice.domain.menu.dto.response;

import com.countrym.deliveryservice.domain.menu.entity.Menu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMenuResponseDto {
    private long menuId;
    private String name;
    private String thumbnailUrl;
    private String details;
    private int price;

    public static GetMenuResponseDto from(Menu menu) {
        return new GetMenuResponseDto(
                menu.getId(),
                menu.getName(),
                menu.getThumbnailUrl(),
                menu.getDetails(),
                menu.getPrice()
        );
    }
}
