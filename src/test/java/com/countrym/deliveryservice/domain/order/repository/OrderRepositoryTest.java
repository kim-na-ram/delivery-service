package com.countrym.deliveryservice.domain.order.repository;

import com.countrym.deliveryservice.common.config.spring.JPAConfig;
import com.countrym.deliveryservice.common.config.util.SnowflakeIdConfig;
import com.countrym.deliveryservice.domain.menu.entity.Menu;
import com.countrym.deliveryservice.domain.menu.repository.MenuRepository;
import com.countrym.deliveryservice.domain.order.dto.projection.OrderSimpleQueryDto;
import com.countrym.deliveryservice.domain.order.dto.projection.StoreOrderQueryDto;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.entity.Order;
import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.enums.PaymentMethod;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.countrym.deliveryservice.domain.data.menu.MenuMockData.getRegisterMenuRequestDto;
import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getRegisterStoreRequestDto;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerSignUpRequestDto;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserSignUpRequestDto;
import static com.countrym.deliveryservice.domain.order.enums.OrderStatus.COOKING;
import static com.countrym.deliveryservice.domain.order.enums.OrderStatus.DELIVERY_COMPLETED;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({JPAConfig.class, SnowflakeIdConfig.class})
@ExtendWith(SpringExtension.class)
class OrderRepositoryTest {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("사용자 주문 목록 조회")
    public class getUserOrderList {
        private final int menuListSize = 5;
        private final int orderListSize = 40;
        private Store store;
        private List<Menu> menuList;
        private List<Order> orderList;

        private long userId;

        @BeforeEach
        public void setup() {
            User owner = userRepository.save(User.from(getOwnerSignUpRequestDto(), "owner123"));

            store = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));

            menuList = new ArrayList<>();
            for (int i = 1; i <= menuListSize; i++) {
                Menu menu = Menu.from(store, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu, "name", "menu " + i);
                ReflectionTestUtils.setField(menu, "price", new Random().nextInt(1000, 15001));

                menuList.add(menu);
            }

            menuList = menuRepository.saveAll(menuList);

            User user = userRepository.save(User.from(getUserSignUpRequestDto(), "user123"));
            userId = user.getId();

            orderList = new ArrayList<>();
            for (int i = 1; i <= orderListSize; i++) {
                List<OrderRequestDto.OrderMenuRequestDto> orderMenuList = new ArrayList<>();
                orderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));
                orderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));

                Order order = Order.from(user, store, new OrderRequestDto(store.getId(), orderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                orderList.add(order);
            }

            orderRepository.saveAll(orderList);
        }

        @Test
        @DisplayName("사용자 주문 목록 조회 시에 정상적으로 paging 처리됨")
        public void getUserOrderList_pagingIsSuccessful() {
            // given
            int pageSize = 10;
            Pageable pageable = PageRequest.of(0, pageSize);

            // when
            List<OrderSimpleQueryDto> orderSimpleList = orderRepository.findListByUserIdAndPageable(userId, null, pageable);

            // then
            assertNotNull(orderSimpleList);
            assertEquals(pageSize, orderSimpleList.size());
        }

        @Test
        @DisplayName("사용자 주문 목록 조회 시에 orderStatus 로 필터링하면 해당되는 주문만 표시됨")
        public void getUserOrderList_filteredByStatus() {
            // given
            int modifiedOrderNumber = 15;
            for (int i = 0; i < modifiedOrderNumber; i++) {
                orderList.get(i).toNextStep(COOKING);
            }
            orderRepository.saveAll(orderList);

            // when
            Pageable pageable = PageRequest.of(0, orderListSize);
            List<OrderSimpleQueryDto> canceledOrderList = orderRepository.findListByUserIdAndPageable(userId, COOKING, pageable);

            // then
            assertNotNull(canceledOrderList);
            assertFalse(canceledOrderList.stream().anyMatch(orderSimpleQueryDto -> orderSimpleQueryDto.getOrderStatus() != COOKING));
        }
    }

    @Nested
    @DisplayName("가게 주문 목록 조회")
    public class getStoreOrderList {
        private final int menuListSize = 10;
        private final int orderListSize = 40;
        private Store store1;
        private Store store2;
        private List<Order> store1OrderList;
        private List<Order> store2OrderList;

        private long storeId;

        @BeforeEach
        public void setup() {
            User owner = userRepository.save(User.from(getOwnerSignUpRequestDto(), "owner123"));

            store1 = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));
            storeId = store1.getId();
            store2 = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));

            List<Menu> menuList1 = new ArrayList<>();
            List<Menu> menuList2 = new ArrayList<>();
            for (int i = 1; i <= menuListSize / 2; i++) {
                Menu menu1 = Menu.from(store1, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu1, "name", "menu " + i);
                ReflectionTestUtils.setField(menu1, "price", new Random().nextInt(1000, 15001));

                menuList1.add(menu1);

                Menu menu2 = Menu.from(store2, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu2, "name", "menu " + i);
                ReflectionTestUtils.setField(menu2, "price", new Random().nextInt(1000, 15001));

                menuList2.add(menu2);
            }

            menuList1 = menuRepository.saveAll(menuList1);
            menuList2 = menuRepository.saveAll(menuList2);

            User user = userRepository.save(User.from(getUserSignUpRequestDto(), "user123"));

            store1OrderList = new ArrayList<>();
            store2OrderList = new ArrayList<>();
            for (int i = 1; i <= orderListSize / 2; i++) {
                List<OrderRequestDto.OrderMenuRequestDto> store1OrderMenuList = new ArrayList<>();
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList1.get(new Random().nextInt(0, menuList1.size())).getId(), new Random().nextInt(1, 3)));
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList1.get(new Random().nextInt(0, menuList1.size())).getId(), new Random().nextInt(1, 3)));

                Order order1 = Order.from(user, store1, new OrderRequestDto(store1.getId(), store1OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                store1OrderList.add(order1);

                List<OrderRequestDto.OrderMenuRequestDto> store2OrderMenuList = new ArrayList<>();
                store2OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList2.get(new Random().nextInt(0, menuList2.size())).getId(), new Random().nextInt(1, 3)));
                store2OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList2.get(new Random().nextInt(0, menuList2.size())).getId(), new Random().nextInt(1, 3)));

                Order order2 = Order.from(user, store2, new OrderRequestDto(store2.getId(), store2OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                store2OrderList.add(order2);
            }

            orderRepository.saveAll(store1OrderList);
            orderRepository.saveAll(store2OrderList);
        }

        @Test
        @DisplayName("가게 주문 목록 조회 시에 정상적으로 paging 처리됨")
        public void getStoreOrderList_pagingIsSuccessful() {
            // given
            int pageSize = 10;
            Pageable pageable = PageRequest.of(0, pageSize);

            // when
            List<StoreOrderQueryDto> storeOrderList = orderRepository.findListByStoreIdAndPageable(store1.getId(), null, pageable);

            // then
            assertNotNull(storeOrderList);
            assertEquals(pageSize, storeOrderList.size());
        }

        @Test
        @DisplayName("가게 주문 목록 조회 시에 orderStatus 로 필터링하면 해당되는 주문만 표시됨")
        public void getStoreOrderList_filteredByStatus() {
            // given
            int modifiedOrderNumber = 7;
            for (int i = 0; i < modifiedOrderNumber; i++) {
                store1OrderList.get(i).toNextStep(COOKING);
            }
            orderRepository.saveAll(store1OrderList);

            // when
            Pageable pageable = PageRequest.of(0, orderListSize);
            List<StoreOrderQueryDto> canceledOrderList = orderRepository.findListByStoreIdAndPageable(storeId, COOKING, pageable);

            // then
            assertNotNull(canceledOrderList);
            assertFalse(canceledOrderList.stream().anyMatch(storeOrderQueryDto -> storeOrderQueryDto.getOrderStatus() != COOKING));
        }

        @Test
        @DisplayName("가게 주문 목록 조회 시에는 해당 가게의 주문 목록만 표시됨")
        public void getStoreOrderList_filteredByStoreId() {
            // given

            // when
            Pageable pageable = PageRequest.of(0, orderListSize);
            List<StoreOrderQueryDto> storeOrderList = orderRepository.findListByStoreIdAndPageable(storeId, null, pageable);

            // then
            assertNotNull(storeOrderList);
            assertFalse(storeOrderList.stream().anyMatch(
                    storeOrderQueryDto -> store2OrderList.stream().anyMatch(order -> order.getId() == storeOrderQueryDto.getOrderId())
            ));
        }
    }

    @Nested
    @DisplayName("사용자의 주문 중 완료되지 않은 주문 존재 여부 확인")
    public class ExistsUnCompletedOrderOfUser {
        private final int totalOrderSize = 5;

        private Store store1;
        private List<Menu> store1MenuList;
        private List<Order> userOrderList;

        private long userId;

        @BeforeEach
        public void setUp() {
            User owner = userRepository.save(User.from(getOwnerSignUpRequestDto(), "owner123"));
            store1 = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));
            Store store2 = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));

            store1MenuList = new ArrayList<>();
            List<Menu> store2MenuList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                Menu menu1 = Menu.from(store1, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu1, "name", "menu " + i);
                ReflectionTestUtils.setField(menu1, "price", new Random().nextInt(1000, 15001));

                store1MenuList.add(menu1);

                Menu menu2 = Menu.from(store2, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu2, "name", "menu " + i);
                ReflectionTestUtils.setField(menu2, "price", new Random().nextInt(1000, 15001));

                store2MenuList.add(menu2);
            }

            store1MenuList = menuRepository.saveAll(store1MenuList);
            store2MenuList = menuRepository.saveAll(store2MenuList);

            User user = userRepository.save(User.from(getUserSignUpRequestDto("user1@google.com"), "user123"));
            userId = user.getId();

            userOrderList = new ArrayList<>();
            for (int i = 1; i <= totalOrderSize; i++) {
                int storeNumber = new Random().nextInt(1, 2);

                if (storeNumber == 1) {
                    List<OrderRequestDto.OrderMenuRequestDto> store1OrderMenuList = new ArrayList<>();
                    store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store1MenuList.get(new Random().nextInt(0, store1MenuList.size())).getId(), new Random().nextInt(1, 3)));
                    store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store1MenuList.get(new Random().nextInt(0, store1MenuList.size())).getId(), new Random().nextInt(1, 3)));

                    Order order1 = Order.from(user, store1, new OrderRequestDto(store1.getId(), store1OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                    userOrderList.add(order1);
                } else {
                    List<OrderRequestDto.OrderMenuRequestDto> store2OrderMenuList = new ArrayList<>();
                    store2OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store2MenuList.get(new Random().nextInt(0, store2MenuList.size())).getId(), new Random().nextInt(1, 3)));
                    store2OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store2MenuList.get(new Random().nextInt(0, store2MenuList.size())).getId(), new Random().nextInt(1, 3)));

                    Order order2 = Order.from(user, store2, new OrderRequestDto(store2.getId(), store2OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                    userOrderList.add(order2);
                }
            }

            orderRepository.saveAll(userOrderList);
        }

        @Test
        @DisplayName("사용자의 주문 중 완료되지 않은 주문들이 존재함")
        public void existsUnCompletedOrderOfUser() {
            // given

            // when
            boolean existsUnCompletedOrderOfUser = orderRepository.existsByUserIdAndStatusNotEquals(userId, OrderStatus.DELIVERY_COMPLETED);

            // then
            assertTrue(existsUnCompletedOrderOfUser);
        }

        @Test
        @DisplayName("사용자의 주문 중 완료되지 않은 주문이 하나라도 존재함")
        public void existsOneUnCompletedOrderOfUser() {
            // given
            for (int i = 0; i < totalOrderSize - 1; i++) {
                userOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }

            orderRepository.saveAll(userOrderList);

            // when
            boolean existsUnCompletedOrderOfUser = orderRepository.existsByUserIdAndStatusNotEquals(userId, DELIVERY_COMPLETED);

            // then
            assertTrue(existsUnCompletedOrderOfUser);
        }

        @Test
        @DisplayName("사용자의 주문 중 완료되지 않은 주문이 존재하지 않음")
        public void notExistsUnCompletedOrderOfUser() {
            // given
            for (int i = 0; i < totalOrderSize; i++) {
                userOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }
            orderRepository.saveAll(userOrderList);

            // when
            boolean existsUnCompletedOrderOfUser = orderRepository.existsByUserIdAndStatusNotEquals(userId, DELIVERY_COMPLETED);

            // then
            assertFalse(existsUnCompletedOrderOfUser);
        }

        @Test
        @DisplayName("다른 유저에게 완료되지 않은 주문이 존재하더라도 정확히 표시됨")
        public void notExistsUnCompletedOrderOfUser_ifOtherUserExistsUnCompletedOrder() {
            // given
            User user2 = userRepository.save(User.from(getUserSignUpRequestDto("user2@google.com"), "user123"));
            long user2Id = user2.getId();

            List<Order> user2OrderList = new ArrayList<>();
            for (int i = 1; i <= totalOrderSize; i++) {
                List<OrderRequestDto.OrderMenuRequestDto> store1OrderMenuList = new ArrayList<>();
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store1MenuList.get(new Random().nextInt(0, store1MenuList.size())).getId(), new Random().nextInt(1, 3)));
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(store1MenuList.get(new Random().nextInt(0, store1MenuList.size())).getId(), new Random().nextInt(1, 3)));

                Order order1 = Order.from(user2, store1, new OrderRequestDto(store1.getId(), store1OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                user2OrderList.add(order1);
            }

            orderRepository.saveAll(user2OrderList);

            for (int i = 0; i < totalOrderSize; i++) {
                userOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }
            orderRepository.saveAll(userOrderList);

            // when
            boolean existsUnCompletedOrderOfUser1 = orderRepository.existsByUserIdAndStatusNotEquals(userId, DELIVERY_COMPLETED);
            boolean existsUnCompletedOrderOfUser2 = orderRepository.existsByUserIdAndStatusNotEquals(user2Id, DELIVERY_COMPLETED);

            // then
            assertFalse(existsUnCompletedOrderOfUser1);
            assertTrue(existsUnCompletedOrderOfUser2);
        }
    }

    @Nested
    @DisplayName("상점에 완료되지 않은 주문 존재 여부 확인")
    public class ExistsUnCompletedOrderInStore {
        private final int totalOrderSize = 5;

        private User owner;
        private User user;

        private List<Order> storeOrderList;

        private long storeId;

        @BeforeEach
        public void setUp() {
            owner = userRepository.save(User.from(getOwnerSignUpRequestDto(), "owner123"));

            Store store = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));
            storeId = store.getId();

            List<Menu> menuList = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Menu menu = Menu.from(store, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu, "name", "menu " + i);
                ReflectionTestUtils.setField(menu, "price", new Random().nextInt(1000, 15001));

                menuList.add(menu);
            }

            menuList = menuRepository.saveAll(menuList);

            user = userRepository.save(User.from(getUserSignUpRequestDto(), "user123"));

            storeOrderList = new ArrayList<>();
            for (int i = 1; i <= totalOrderSize; i++) {
                List<OrderRequestDto.OrderMenuRequestDto> store1OrderMenuList = new ArrayList<>();
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));
                store1OrderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));

                Order order = Order.from(user, store, new OrderRequestDto(store.getId(), store1OrderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                storeOrderList.add(order);
            }

            orderRepository.saveAll(storeOrderList);
        }

        @Test
        @DisplayName("상점에 완료되지 않은 주문들이 존재함")
        public void existsUnCompletedOrderInStore() {
            // given

            // when
            boolean existsUnCompletedOrderInStore = orderRepository.existsByStoreIdAndStatusNotEquals(storeId, OrderStatus.DELIVERY_COMPLETED);

            // then
            assertTrue(existsUnCompletedOrderInStore);
        }

        @Test
        @DisplayName("상점에 완료되지 않은 주문이 하나라도 존재함")
        public void existsOneUnCompletedOrderInStore() {
            // given
            for (int i = 0; i < totalOrderSize - 1; i++) {
                storeOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }

            orderRepository.saveAll(storeOrderList);

            // when
            boolean existsUnCompletedOrderInStore = orderRepository.existsByStoreIdAndStatusNotEquals(storeId, DELIVERY_COMPLETED);

            // then
            assertTrue(existsUnCompletedOrderInStore);
        }

        @Test
        @DisplayName("상점에 완료되지 않은 주문이 존재하지 않음")
        public void notExistsUnCompletedOrderInStore() {
            // given
            for (int i = 0; i < totalOrderSize; i++) {
                storeOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }
            orderRepository.saveAll(storeOrderList);

            // when
            boolean existsUnCompletedOrderInStore1 = orderRepository.existsByStoreIdAndStatusNotEquals(storeId, DELIVERY_COMPLETED);

            // then
            assertFalse(existsUnCompletedOrderInStore1);
        }

        @Test
        @DisplayName("타 상점에 완료되지 않은 주문이 존재하더라도 정확히 표시됨")
        public void notExistsUnCompletedOrderInStore_ifOtherStoreExistsUnCompletedOrder() {
            // given
            Store store = storeRepository.save(Store.from(owner, getRegisterStoreRequestDto()));
            long store2Id = store.getId();

            List<Menu> menuList = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Menu menu2 = Menu.from(store, getRegisterMenuRequestDto());
                ReflectionTestUtils.setField(menu2, "name", "menu " + i);
                ReflectionTestUtils.setField(menu2, "price", new Random().nextInt(1000, 15001));
                menuList.add(menu2);
            }

            menuList = menuRepository.saveAll(menuList);

            List<Order> store2OrderList = new ArrayList<>();
            for (int i = 1; i <= totalOrderSize; i++) {
                List<OrderRequestDto.OrderMenuRequestDto> orderMenuList = new ArrayList<>();
                orderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));
                orderMenuList.add(new OrderRequestDto.OrderMenuRequestDto(menuList.get(new Random().nextInt(0, menuList.size())).getId(), new Random().nextInt(1, 3)));

                Order order2 = Order.from(user, store, new OrderRequestDto(store.getId(), orderMenuList, new Random().nextInt(10000, 50001), PaymentMethod.CARD.name()));
                store2OrderList.add(order2);
            }

            orderRepository.saveAll(store2OrderList);

            for (int i = 0; i < totalOrderSize; i++) {
                storeOrderList.get(i).toNextStep(DELIVERY_COMPLETED);
            }
            orderRepository.saveAll(storeOrderList);

            // when
            boolean existsUnCompletedOrderInStore1 = orderRepository.existsByStoreIdAndStatusNotEquals(storeId, DELIVERY_COMPLETED);
            boolean existsUnCompletedOrderInStore2 = orderRepository.existsByStoreIdAndStatusNotEquals(store2Id, DELIVERY_COMPLETED);

            // then
            assertFalse(existsUnCompletedOrderInStore1);
            assertTrue(existsUnCompletedOrderInStore2);
        }
    }
}