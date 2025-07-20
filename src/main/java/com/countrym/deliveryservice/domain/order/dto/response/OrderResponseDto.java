package com.countrym.deliveryservice.domain.order.dto.response;

import com.countrym.deliveryservice.domain.menu.dto.response.OrderedMenuResponseDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderDetailQueryDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class OrderResponseDto {
    private final long orderId;
    private final String orderUserName;
    private final long storeId;
    private final String storeName;
    private final List<OrderedMenuResponseDto> menuList;
    private final int orderedPrice;
    private final String paymentMethod;
    private final String orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime canceledAt;

    private OrderResponseDto(
            long orderId,
            String orderUserName,
            long storeId,
            String storeName,
            List<OrderedMenuResponseDto> menuList,
            int orderedPrice,
            PaymentMethod paymentMethod,
            OrderStatus orderStatus,
            LocalDateTime orderedAt,
            LocalDateTime canceledAt
    ) {
        this.orderId = orderId;
        this.orderUserName = orderUserName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.menuList = menuList;
        this.orderedPrice = orderedPrice;
        this.paymentMethod = paymentMethod.getDescription();
        this.orderStatus = orderStatus.getDescription();
        this.orderedAt = orderedAt;
        this.canceledAt = canceledAt;
    }

    public static OrderResponseDto from(OrderDetailQueryDto userOrderDto) {
        return new OrderResponseDto(
                userOrderDto.getOrderId(),
                userOrderDto.getOrderUserName(),
                userOrderDto.getStoreId(),
                userOrderDto.getStoreName(),
                userOrderDto.getMenuList().stream().map(OrderedMenuResponseDto::from).toList(),
                userOrderDto.getOrderedPrice(),
                userOrderDto.getPaymentMethod(),
                userOrderDto.getOrderStatus(),
                userOrderDto.getOrderedAt(),
                userOrderDto.getCanceledAt()
        );
    }
}
