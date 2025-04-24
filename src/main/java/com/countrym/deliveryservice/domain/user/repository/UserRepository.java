package com.countrym.deliveryservice.domain.user.repository;

import com.countrym.deliveryservice.common.exception.NotFoundException;
import com.countrym.deliveryservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.countrym.deliveryservice.common.exception.ResponseCode.NOT_FOUND_USER;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    default User findUserById(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }
}