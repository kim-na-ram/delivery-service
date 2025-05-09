package com.countrym.deliveryservice.domain.store.entity;

import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "store_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotBlank
    private String name;

    private String thumbnailUrl;

    private String details;

    @NotBlank
    private String address1;

    private String address2;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StoreType type;

    @NotNull
    @Column(name = "open_at")
    private LocalTime openAt;

    @NotNull
    @Column(name = "closed_at")
    private LocalTime closedAt;

    @NotNull
    @Column(name = "min_order_price")
    private int minOrderPrice;

    @NotNull
    @Column(name = "average_rating")
    private double averageRating;

    @NotNull
    @Column(name = "review_amount")
    private int reviewAmount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Store(
            User user,
            String name,
            String details,
            String address1,
            String address2,
            String type,
            LocalTime openAt,
            LocalTime closedAt,
            int minOrderPrice
    ) {
        this.owner = user;
        this.name = name;
        this.details = details;
        this.address1 = address1;
        this.address2 = address2;
        this.type = StoreType.of(type);
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.minOrderPrice = minOrderPrice;
        this.averageRating = 0.0;
        this.reviewAmount = 0;
    }

    public static Store from(User user, RegisterStoreRequestDto registerStoreRequestDto) {
        return new Store(
                user,
                registerStoreRequestDto.getName(),
                registerStoreRequestDto.getDetails(),
                registerStoreRequestDto.getAddress1(),
                registerStoreRequestDto.getAddress2(),
                registerStoreRequestDto.getType(),
                registerStoreRequestDto.getOpenAt(),
                registerStoreRequestDto.getClosedAt(),
                registerStoreRequestDto.getMinOrderPrice()
        );
    }

    public void modify(ModifyStoreRequestDto modifyStoreRequestDto) {
        if (!modifyStoreRequestDto.getName().isBlank()) this.name = modifyStoreRequestDto.getName();
        if (!modifyStoreRequestDto.getDetails().isBlank()) this.details = modifyStoreRequestDto.getDetails();
        if (!modifyStoreRequestDto.getAddress1().isBlank()) this.address1 = modifyStoreRequestDto.getAddress1();
        if (!modifyStoreRequestDto.getAddress2().isBlank()) this.address2 = modifyStoreRequestDto.getAddress2();
        if (!modifyStoreRequestDto.getType().isBlank()) this.type = StoreType.of(modifyStoreRequestDto.getType());
        if (modifyStoreRequestDto.getOpenAt() != null) this.openAt = modifyStoreRequestDto.getOpenAt();
        if (modifyStoreRequestDto.getClosedAt() != null) this.closedAt = modifyStoreRequestDto.getClosedAt();
        if (modifyStoreRequestDto.getMinOrderPrice() != null) this.minOrderPrice = modifyStoreRequestDto.getMinOrderPrice();
    }

    public void closure() {
        this.deletedAt = LocalDateTime.now();
    }
}