package com.countrym.deliveryservice.domain.order.entity;

import com.countrym.deliveryservice.common.entity.generator.SnowflakeId;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.enums.PaymentMethod;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "order_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {
    @Id
    @SnowflakeId
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotNull
    @Column(name = "price")
    private int price;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @NotNull
    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderedMenu> orderedMenuList = new ArrayList<>();

    private Order(long orderId) {
        this.id = orderId;
    }

    private Order(User user, Store store, int price, PaymentMethod paymentMethod, LocalDateTime orderedAt) {
        this.user = user;
        this.store = store;
        this.status = OrderStatus.PENDING_ACCEPTANCE;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.orderedAt = orderedAt;
        this.orderedMenuList = new ArrayList<>();
    }

    public static Order of(long orderId) {
        return new Order(orderId);
    }

    public static Order from(User user, Store store, OrderRequestDto orderRequestDto) {
        Order order = new Order(
                user,
                store,
                orderRequestDto.getOrderedPrice(),
                PaymentMethod.of(orderRequestDto.getPaymentMethod()),
                LocalDateTime.now()
        );

        orderRequestDto.getMenuList().forEach(menu -> {
            OrderedMenu orderedMenu = OrderedMenu.of(order, Menu.of(menu.getMenuId()));
            orderedMenu.addOrderedMenu(menu.getQuantity(), order);
        });

        return order;
    }

    public void toNextStep(OrderStatus nextStatus) {
        this.status = nextStatus;
    }

    public void cancel() {
        this.canceledAt = LocalDateTime.now();
        this.status = OrderStatus.CANCELED;
    }

}
