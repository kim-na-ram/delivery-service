package com.countrym.deliveryservice.domain.order.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderDetailQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.request.ModifyOrderStatusRequestDto;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.dto.response.OrderListResponseDto;
import com.countrym.deliveryservice.domain.order.dto.response.OrderResponseDto;
import com.countrym.deliveryservice.domain.order.dto.response.StoreOrderListResponseDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.facade.OrderFacade;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderFacade orderFacade;

    @Transactional
    public void order(UserInfo userInfo, OrderRequestDto orderRequestDto) {
        Store store = orderFacade.getStore(orderRequestDto.getStoreId());

        if (!checkIsOpen(LocalTime.now(), store.getOpenAt(), store.getClosedAt())) {
            throw new InvalidParameterException(INVALID_STORE_STATUS);
        }

        // 최소 주문 가격 validate
        if (store.getMinOrderPrice() > orderRequestDto.getOrderedPrice())
            throw new InvalidParameterException(INVALID_MINIMAL_ORDER_PRICE);

        // 메뉴 validate
        Map<Long, Integer> storeMenuList = store.getMenuList().stream()
                .collect(Collectors.toMap(Menu::getId, Menu::getPrice));

        if (orderRequestDto.getMenuList().stream()
                .anyMatch(orderedMenu -> !storeMenuList.containsKey(orderedMenu.getMenuId())))
            throw new NotFoundException(NOT_FOUND_STORE_MENU);

        // 총 주문 가격 validate
        // 주문받은 메뉴 ID와 수량을 storeMenuList 의 메뉴 가격과 곱셈 후, 모두 더함
        int calculatedPrice = orderRequestDto.getMenuList()
                .stream().mapToInt(menu -> menu.getQuantity() * storeMenuList.get(menu.getMenuId())).sum();
        if (calculatedPrice != orderRequestDto.getOrderedPrice())
            throw new InvalidParameterException(INVALID_ORDERED_PRICE);

        Order order = Order.from(User.from(userInfo), Store.of(orderRequestDto.getStoreId()), orderRequestDto);
        store.plusTotalOrderCount(1);
        orderFacade.saveOrderAndStore(order, store);
    }

    @Transactional
    public void modifyOrderStatus(UserInfo userInfo, long orderId, ModifyOrderStatusRequestDto modifyOrderStatusRequestDto) {
        OrderQueryDto orderQueryDto = orderFacade.getOrder(orderId);

        if (orderQueryDto.getOwnerId() != userInfo.getId())
            throw new InvalidParameterException(UNAUTHORIZED_CHANGE_ORDER_STATUS);

        Order order = orderQueryDto.getOrder();

        OrderStatus currentOrderStatus = order.getStatus();
        OrderStatus nextOrderStatus = OrderStatus.of(modifyOrderStatusRequestDto.getOrderStatus());

        if (OrderStatus.isNextStep(currentOrderStatus, nextOrderStatus)
                && currentOrderStatus == OrderStatus.CANCELED)
            throw new InvalidParameterException(INVALID_CHANGE_ORDER_STATUS_FROM_CANCEL);

        if (!OrderStatus.isNextStep(currentOrderStatus, nextOrderStatus))
            throw new InvalidParameterException(INVALID_CHANGE_ORDER_STATUS);

        order.toNextStep(nextOrderStatus);

        if (nextOrderStatus == OrderStatus.CANCELED) {
            Store store = orderFacade.getStore(orderQueryDto.getStoreId());
            store.minusTotalOrderCount(1);
            orderFacade.saveOrderAndStore(order, store);
        } else {
            orderFacade.saveOrder(order);
        }
    }

    @Transactional
    public void cancelOrder(UserInfo userInfo, long orderId) {
        OrderQueryDto orderQueryDto = orderFacade.getOrder(orderId);
        Store store = orderFacade.getStore(orderQueryDto.getStoreId());

        if (orderQueryDto.getOrderUserId() != userInfo.getId())
            throw new NotFoundException(NOT_FOUND_ORDER);

        Order order = orderQueryDto.getOrder();

        if (OrderStatus.isAlreadyCancel(order.getStatus()))
            throw new InvalidParameterException(ALREADY_CANCELED_ORDER_ERROR);

        if (!OrderStatus.isPossibleCancel(order.getStatus()))
            throw new InvalidParameterException(INVALID_CANCEL_ORDER_FOR_USER);

        order.cancel();
        store.minusTotalOrderCount(1);

        // TODO refund 추가

        orderFacade.saveOrderAndStore(order, store);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UserInfo userInfo, long orderId) {
        OrderDetailQueryDto userOrderDto = orderFacade.getOrderDetail(orderId);

        if (userInfo.getAuthority().isOwner() && userOrderDto.getStoreOwnerId() != userInfo.getId()) {
            throw new NotFoundException(NOT_FOUND_ORDER);
        } else if (userInfo.getAuthority().isUser() && userOrderDto.getOrderUserId() != userInfo.getId()) {
            throw new NotFoundException(NOT_FOUND_ORDER);
        }

        return OrderResponseDto.from(userOrderDto);
    }

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getOrderListByPaging(UserInfo userInfo, String status, Pageable pageable) {
        List<OrderSimpleQueryDto> orderSimpleList =
                orderFacade.getOrderList(
                        userInfo.getId(),
                        userInfo.getAuthority(),
                        stringStatusToOrderStatus(status),
                        pageable
                );
        return orderSimpleList.stream().map(OrderListResponseDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StoreOrderListResponseDto> getOrderListWithStore(UserInfo userInfo, long storeId, String status, Pageable pageable) {
        List<StoreOrderQueryDto> storeOrderDtoList =
                orderFacade.getStoreOrderList(storeId, stringStatusToOrderStatus(status), pageable);

        if (storeOrderDtoList.isEmpty()) return List.of();

        StoreOrderQueryDto storeOrderQueryDto = storeOrderDtoList.get(0);
        if (storeOrderQueryDto.getStoreOwnerId() != userInfo.getId())
            throw new InvalidParameterException(INVALID_STORE_OWNER);

        return storeOrderDtoList.stream().map(StoreOrderListResponseDto::from).toList();
    }

    private boolean checkIsOpen(LocalTime now, LocalTime openAt, LocalTime closedAt) {
        boolean isOpen;
        if (closedAt.getHour() <= 6 && now.getHour() <= 6) {
            // 만약 closedAt이 새벽 6시 이전인데, 현재 시각도 새벽 6시 이전이라면 (ex. 2시)
            // closedAt 이전인지 확인
            isOpen = now.isBefore(closedAt);
        } else if (closedAt.getHour() <= 6) {
            // 만약 closedAt이 새벽 6시 이전인데, 현재 시각이 새벽 6시보다 크다면 (ex. 11시)
            // openAt 이후인지 확인
            isOpen = now.isAfter(openAt);
        } else {
            // 만약 closedAt이 새벽 6시 이후라면
            isOpen = !now.isBefore(openAt) && !now.isAfter(closedAt);
        }

        return isOpen;
    }

    private OrderStatus stringStatusToOrderStatus(String status) {
        return StringUtils.hasText(status) ? OrderStatus.of(status) : null;
    }
}
