package com.countrym.deliveryservice.domain.order.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import lombok.Getter;

import java.util.Arrays;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;

@Getter
public enum OrderStatus implements BaseEnum {
    PENDING_ACCEPTANCE("수락 대기", 0),
    CANCELED("주문 취소", 1),
    COOKING("조리중", 2),
    DELIVERY_STARTED("배달 시작", 3),
    DELIVERY_COMPLETED("배달 완료", 4);

    private final String description;
    private final int step;

    OrderStatus(String description, int step) {
        this.description = description;
        this.step = step;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static OrderStatus of(String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(o -> o.name().equals(name))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException(INVALID_ORDER_STATUS));
    }

    public static boolean isAlreadyCancel(OrderStatus status) {
        return status == OrderStatus.CANCELED;
    }

    public static boolean isPossibleCancel(OrderStatus status) {
        return status == OrderStatus.PENDING_ACCEPTANCE;
    }

    public static boolean isCompleted(OrderStatus status) {
        return status == OrderStatus.DELIVERY_COMPLETED;
    }

    public static boolean isNextStep(OrderStatus status, OrderStatus nextStatus) {
        return status.getStep() < nextStatus.getStep();
    }

}
