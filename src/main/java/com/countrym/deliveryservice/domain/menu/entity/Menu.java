package com.countrym.deliveryservice.domain.menu.entity;

import com.countrym.deliveryservice.domain.menu.dto.request.RegisterMenuRequestDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "menu_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "price")
    private int price;

    private String thumbnailUrl;

    @Column(name = "details")
    private String details;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Menu(Store store, String name, String thumbnailUrl, String details, int price) {
        this.store = store;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.details = details;
        this.price = price;
    }

    public static Menu from(Store store, RegisterMenuRequestDto registerMenuRequestDto) {
        return new Menu(
                store,
                registerMenuRequestDto.getName(),
                registerMenuRequestDto.getThumbnailUrl(),
                registerMenuRequestDto.getDetails(),
                registerMenuRequestDto.getPrice()
        );
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
