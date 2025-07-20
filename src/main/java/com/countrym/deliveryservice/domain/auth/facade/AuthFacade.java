package com.countrym.deliveryservice.domain.auth.facade;

import com.countrym.deliveryservice.domain.order.enums.OrderStatus;
import com.countrym.deliveryservice.domain.order.repository.OrderRepository;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUser(long userId) {
        return userRepository.findUserById(userId);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public boolean existsOpenedStore(long ownerId) {
        return storeRepository.existsOpenedStoreByOwnerId(ownerId);
    }

    public boolean existUncompletedOrder(long userId) {
        return orderRepository.existsByUserIdAndStatusNotEquals(userId, OrderStatus.DELIVERY_COMPLETED);
    }

    public void deleteUser(User user) {
        user.deleteUser();
        userRepository.save(user);
    }
}
