package com.countrym.deliveryservice.domain.order.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.request.ModifyOrderStatusRequestDto;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.facade.OrderFacade;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.enums.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.countrym.deliveryservice.common.exception.ResponseCode.*;
import static com.countrym.deliveryservice.domain.data.menu.MenuMockData.getMenuList;
import static com.countrym.deliveryservice.domain.data.order.OrderMockData.*;
import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStore;
import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getStoreWithMenuList;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserInfo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderFacade orderFacade;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("주문")
    class RegisterOrder {
        @Test
        @DisplayName("주문한 가게가 열려있지 않아 주문에 실패")
        public void order_storeIsNotOpened_failure() {
            // given
            UserInfo userInfo = getUserInfo();
            OrderRequestDto orderRequestDto = getOrderRequestDto();

            User owner = User.from(getOwnerInfo());
            RegisterStoreRequestDto registerStoreRequestDto
                    = new RegisterStoreRequestDto(
                    "name",
                    "thumbnailUrl",
                    "details",
                    "address1",
                    "address2",
                    StoreType.CAFE.name(),
                    LocalTime.of(LocalDateTime.now().plusHours(1).getHour(), 0),
                    LocalTime.of(LocalDateTime.now().plusHours(3).getHour(), 0),
                    10000
            );

            Store store = Store.from(owner, registerStoreRequestDto);

            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.order(userInfo, orderRequestDto));

            // then
            assertEquals(INVALID_STORE_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 가격이 가게 최소 주문 가격보다 적어 주문에 실패")
        public void order_orderPriceLessThanMinOrderPrice_failure() {
            // given
            UserInfo userInfo = getUserInfo();
            OrderRequestDto orderRequestDto = getOrderRequestDto_lessThanMinOrderPrice();

            Store store = getStore(getOwnerInfo());

            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.order(userInfo, orderRequestDto));

            // then
            assertEquals(INVALID_MINIMAL_ORDER_PRICE.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 가격이 계산된 가격과 맞지 않아 주문에 실패")
        public void order_incorrectOrderedPrice_failure() {
            // given
            UserInfo userInfo = getUserInfo();
            OrderRequestDto orderRequestDto = getOrderRequestDto();

            Store store = getStoreWithMenuList();

            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.order(userInfo, orderRequestDto));

            // then
            assertEquals(INVALID_ORDERED_PRICE.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("성공")
        public void order_success() {
            // given
            UserInfo userInfo = getUserInfo();
            OrderRequestDto orderRequestDto = getOrderRequestDto();

            Store store = getStoreWithMenuList();
            int prevTotalOrderCount = store.getTotalOrderCount();
            List<Menu> menuList = getMenuList(null);

            int sumOrderedPrice = 0;
            for (OrderRequestDto.OrderMenuRequestDto orderedMenu : orderRequestDto.getMenuList()) {
                int cal = menuList.get(orderedMenu.getMenuId().intValue()).getPrice() * orderedMenu.getQuantity();
                sumOrderedPrice += cal;
            }

            ReflectionTestUtils.setField(orderRequestDto, "orderedPrice", sumOrderedPrice);

            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when, then
            assertDoesNotThrow(() -> orderService.order(userInfo, orderRequestDto));
            assertNotEquals(prevTotalOrderCount, store.getTotalOrderCount());
        }
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class ModifyOrderStatus {
        @Test
        @DisplayName("사용자가 해당 가게의 점주가 아닐 경우 주문 상태 변경에 실패")
        public void modifyOrderStatus_userIsNotStoreOwner_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getUserInfo();
            ModifyOrderStatusRequestDto modifyOrderStatusRequestDto = getModifyOrderStatusRequestDto();

            OrderQueryDto orderQueryDto = getOrderQueryDto();

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto));

            // then
            assertEquals(UNAUTHORIZED_CHANGE_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("이미 취소된 주문일 경우 주문 상태 변경에 실패")
        public void modifyOrderStatus_alreadyCanceledOrder_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyOrderStatusRequestDto modifyOrderStatusRequestDto = getModifyOrderStatusRequestDto();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            ReflectionTestUtils.setField(orderQueryDto.getOrder(), "status", OrderStatus.CANCELED);

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto));

            // then
            assertEquals(INVALID_CHANGE_ORDER_STATUS_FROM_CANCEL.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("현재의 주문 상태보다 이전의 주문 상태일 경우 주문 상태 변경에 실패")
        public void modifyOrderStatus_invalidOrderStatus_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyOrderStatusRequestDto modifyOrderStatusRequestDto = getModifyOrderStatusRequestDto();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            ReflectionTestUtils.setField(orderQueryDto.getOrder(), "status", OrderStatus.COOKING);

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);

            // when
            Throwable t = assertThrows(InvalidParameterException.class,
                    () -> orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto));

            // then
            assertEquals(INVALID_CHANGE_ORDER_STATUS.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("성공 (주문 취소)")
        public void modifyOrderStatus_cancel_success() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyOrderStatusRequestDto modifyOrderStatusRequestDto = new ModifyOrderStatusRequestDto(OrderStatus.CANCELED.name());

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            Order order = orderQueryDto.getOrder();
            OrderStatus prevOrderStatus = order.getStatus();

            Store store = getStore(getOwnerInfo());
            int prevTotalOrderCount = store.getTotalOrderCount();

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);
            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto);

            // then
            assertNotEquals(prevOrderStatus, order.getStatus());
            assertNotEquals(prevTotalOrderCount, store.getTotalOrderCount());
            assertEquals(OrderStatus.CANCELED, order.getStatus());
        }

        @Test
        @DisplayName("성공")
        public void modifyOrderStatus_success() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyOrderStatusRequestDto modifyOrderStatusRequestDto = getModifyOrderStatusRequestDto();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            Order order = orderQueryDto.getOrder();
            OrderStatus prevOrderStatus = order.getStatus();

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);

            // when
            orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto);

            // then
            assertNotEquals(prevOrderStatus, order.getStatus());
        }
    }

    @Nested
    @DisplayName("주문 취소")
    class CancelOrder {
        @Test
        @DisplayName("사용자가 주문한 사용자가 아닐 경우 주문 취소에 실패")
        public void cancelOrder_userIsNotOrderUser_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = UserInfo.of(3L, Authority.USER);

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            Store store = getStore(getOwnerInfo());

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);
            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(NotFoundException.class, () -> orderService.cancelOrder(userInfo, orderId));

            // then
            assertEquals(NOT_FOUND_ORDER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("이미 취소된 주문일 경우 주문 상태 변경에 실패")
        public void cancelOrder_alreadyCanceledOrder_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getUserInfo();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            ReflectionTestUtils.setField(orderQueryDto.getOrder(), "status", OrderStatus.CANCELED);

            Store store = getStore(getOwnerInfo());

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);
            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.cancelOrder(userInfo, orderId));

            // then
            assertEquals(ALREADY_CANCELED_ORDER_ERROR.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("주문 수락 이외의 주문 상태일 경우 주문 취소에 실패")
        public void cancelOrder_invalidOrderStatus_failure() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getUserInfo();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            ReflectionTestUtils.setField(orderQueryDto.getOrder(), "status", OrderStatus.DELIVERY_COMPLETED);

            Store store = getStore(getOwnerInfo());

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);
            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when
            Throwable t = assertThrows(InvalidParameterException.class, () -> orderService.cancelOrder(userInfo, orderId));

            // then
            assertEquals(INVALID_CANCEL_ORDER_FOR_USER.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("성공")
        public void cancelOrder_success() {
            // given
            long orderId = 1L;
            UserInfo userInfo = getUserInfo();

            OrderQueryDto orderQueryDto = getOrderQueryDto();
            Order order = orderQueryDto.getOrder();

            Store store = getStore(getOwnerInfo());
            int totalOrderCount = store.getTotalOrderCount();

            given(orderFacade.getOrder(anyLong())).willReturn(orderQueryDto);
            given(orderFacade.getStore(anyLong())).willReturn(store);

            // when, then
            assertDoesNotThrow(() -> orderService.cancelOrder(userInfo, orderId));
            assertEquals(OrderStatus.CANCELED, order.getStatus());
            assertNotEquals(totalOrderCount, store.getTotalOrderCount());
        }
    }
}