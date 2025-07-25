package com.countrym.deliveryservice.domain.data.order;

import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.request.ModifyOrderStatusRequestDto;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.enums.PaymentMethod;
import com.countrym.deliveryservice.domain.user.entity.User;

import java.util.List;

import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStore;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserInfo;

public class OrderMockData {
    public static OrderRequestDto.OrderMenuRequestDto getOrderMenuRequestDto() {
        return new OrderRequestDto.OrderMenuRequestDto(
                1L,
                1
        );
    }

    public static OrderRequestDto getOrderRequestDto() {
        return new OrderRequestDto(
                1L,
                List.of(getOrderMenuRequestDto()),
                11000,
                PaymentMethod.NAVER_PAY.name()
        );
    }

    public static OrderRequestDto getOrderRequestDto(long storeId, long menuId, int quantity, int orderedPrice) {
        return new OrderRequestDto(
                storeId,
                List.of(new OrderRequestDto.OrderMenuRequestDto(menuId, quantity)),
                orderedPrice,
                PaymentMethod.CARD.name()
        );
    }

    public static OrderRequestDto getOrderRequestDto_lessThanMinOrderPrice() {
        return new OrderRequestDto(
                1L,
                List.of(getOrderMenuRequestDto()),
                1000,
                PaymentMethod.NAVER_PAY.name()
        );
    }

    public static OrderQueryDto getOrderQueryDto() {
        return new OrderQueryDto(
                1L,
                getOwnerInfo().getId(),
                getUserInfo().getId(),
                getOrder()
        );
    }

    public static ModifyOrderStatusRequestDto getModifyOrderStatusRequestDto() {
        return new ModifyOrderStatusRequestDto(OrderStatus.COOKING.name());
    }

    public static Order getOrder() {
        return Order.from(
                User.from(getUserInfo()),
                getStore(getOwnerInfo()),
                getOrderRequestDto()
        );
    }
}