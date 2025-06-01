package com.countrym.deliveryservice.domain.store.enums;

import com.countrym.deliveryservice.common.enums.BaseEnum;

public enum StoreSortType implements BaseEnum {
    MANY_ORDERS("주문 많은 순"),
    HIGH_AVERAGE_RATING("별점 높은 순"),
    ;

    private final String description;

    StoreSortType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
