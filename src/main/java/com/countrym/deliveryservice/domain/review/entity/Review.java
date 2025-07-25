package com.countrym.deliveryservice.domain.review.entity;

import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.review.dto.request.RegisterReviewRequestDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.user.entity.User;
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
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    private double rating;

    @NotBlank
    private String details;

    @NotNull
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Review(
            User user,
            Order order,
            Store store,
            double rating,
            String details,
            LocalDateTime reviewedAt
    ) {
        this.reviewer = user;
        this.order = order;
        this.store = store;
        this.rating = rating;
        this.details = details;
        this.reviewedAt = reviewedAt;
    }

    public static Review from(User user, Order order, Store store, RegisterReviewRequestDto reviewRequestDto) {
        return new Review(
                user,
                order,
                store,
                reviewRequestDto.getRating(),
                reviewRequestDto.getDetails(),
                LocalDateTime.now()
        );
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
