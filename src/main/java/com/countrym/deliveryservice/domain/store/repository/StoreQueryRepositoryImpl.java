package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.QGetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.countrym.deliveryservice.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetStoreListResponseDto> findAllByTypeAndName(StoreType type, String name, Pageable pageable) {
        return jpaQueryFactory.select(
                        new QGetStoreListResponseDto(
                                store.id,
                                store.name,
                                store.thumbnailUrl,
                                store.openAt,
                                store.closedAt,
                                store.minOrderPrice,
                                store.averageRating,
                                store.reviewAmount
                        )
                )
                .from(store)
                .where(
                        storeTypeEquals(type),
                        storeNameEquals(name)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy()
                .fetch();
    }

    private BooleanExpression storeTypeEquals(StoreType type) {
        return type == null ? null : store.type.eq(type);
    }

    private BooleanExpression storeNameEquals(String name) {
        return StringUtils.hasText(name) ? store.name.contains(name) : null;
    }
}
