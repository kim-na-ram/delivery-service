package com.countrym.deliveryservice.domain.store.facade;

import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.repository.OrderRepository;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreFacade {
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    public boolean existsByName(String name) {
        return storeRepository.existsByName(name);
    }

    public boolean existUncompletedOrderInStore(long ownerId) {
        return orderRepository.existsByStoreIdAndStatusNotEquals(ownerId, OrderStatus.DELIVERY_COMPLETED);
    }

    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    public Store getStore(long storeId) {
        return storeRepository.findByStoreId(storeId);
    }

    public StoreMenuListDto getStoreWithMenuList(long storeId) {
        return storeRepository.findByStoreIdWithMenuList(storeId);
    }

    public List<GetStoreListResponseDto> getStoreByPaging(StoreType storeType, String name, Pageable pageable) {
        return storeRepository.findAllByTypeAndNameByPaging(storeType, name, pageable);
    }
}
