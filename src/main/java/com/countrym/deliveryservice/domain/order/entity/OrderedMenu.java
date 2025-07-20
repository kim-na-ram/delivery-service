package com.countrym.deliveryservice.domain.order.entity;

import com.countrym.deliveryservice.common.entity.generator.SnowflakeId;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ordered_menu_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderedMenu {
    @Id
    @SnowflakeId
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int quantity;

    private OrderedMenu(Order order, Menu menu) {
        this.order = order;
        this.menu = menu;
    }

    public static OrderedMenu of(Order order, Menu menu) {
        return new OrderedMenu(order, menu);
    }

    public void addOrderedMenu(int quantity, Order order) {
        this.quantity = quantity;
        this.order = order;
        order.getOrderedMenuList().add(this);
    }
}
