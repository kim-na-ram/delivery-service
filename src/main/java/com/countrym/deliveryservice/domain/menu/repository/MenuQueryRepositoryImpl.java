package com.countrym.deliveryservice.domain.menu.repository;

import com.countrym.deliveryservice.domain.menu.dto.projection.QStoreOwnerMenuDto;
import com.countrym.deliveryservice.domain.menu.dto.projection.StoreOwnerMenuDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.countrym.deliveryservice.domain.menu.entity.QMenu.menu;
import static com.countrym.deliveryservice.domain.store.entity.QStore.store;
import static com.countrym.deliveryservice.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class MenuQueryRepositoryImpl implements MenuQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StoreOwnerMenuDto> findByIdAndStoreId(long menuId, long storeId) {
        return Optional.ofNullable(jpaQueryFactory.select(
                        new QStoreOwnerMenuDto(
                                menu.store.id,
                                menu.store.owner.id,
                                menu.id,
                                menu.name,
                                menu.thumbnailUrl,
                                menu.details,
                                menu.price
                        )
                )
                .from(menu)
                .join(menu.store, store)
                .join(store.owner, user)
                .where(
                        menu.id.eq(menuId),
                        menu.store.id.eq(storeId)
                )
                .fetchFirst());
    }
}
