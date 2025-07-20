package com.countrym.deliveryservice.domain.menu.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuQueryDto;
import com.countrym.deliveryservice.domain.menu.dto.projection.OrderedMenuSimpleQueryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderedMenuResponseDto {
    private final long menuId;
    private final String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer price;
    private final int quantity;

    private OrderedMenuResponseDto(long menuId, String name, int quantity) {
        this.menuId = menuId;
        this.name = name;
        this.quantity = quantity;
    }

    public static OrderedMenuResponseDto from(OrderedMenuQueryDto orderedMenuDto) {
        return new OrderedMenuResponseDto(
                orderedMenuDto.getMenuId(),
                orderedMenuDto.getName(),
                orderedMenuDto.getPrice(),
                orderedMenuDto.getQuantity()
        );
    }

    public static OrderedMenuResponseDto from(OrderedMenuSimpleQueryDto orderedMenuSimpleQueryDto) {
        return new OrderedMenuResponseDto(
                orderedMenuSimpleQueryDto.getMenuId(),
                orderedMenuSimpleQueryDto.getName(),
                orderedMenuSimpleQueryDto.getQuantity()
        );
    }
}


