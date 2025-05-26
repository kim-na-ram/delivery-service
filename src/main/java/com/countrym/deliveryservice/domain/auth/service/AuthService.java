package com.countrym.deliveryservice.domain.auth.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.DuplicateElementException;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.util.JwtUtils;
import com.countrym.deliveryservice.domain.auth.dto.request.SignInRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.SignUpRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.WithdrawRequestDto;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.enums.Authority;
import com.countrym.deliveryservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.countrym.deliveryservice.common.config.security.TokenConst.ACCESS_TOKEN_EXPIRE_TIME;
import static com.countrym.deliveryservice.common.exception.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateElementException(ALREADY_SIGNED_EMAIL_ERROR);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User newUser = User.from(signUpRequestDto, encodedPassword);

        userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public String signIn(SignInRequestDto signInRequestDto) {
        User user = userRepository.findByEmail(signInRequestDto.getEmail())
                .orElseThrow(() -> new InvalidParameterException(INVALID_EMAIL_OR_PASSWORD));

        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(INVALID_EMAIL_OR_PASSWORD);
        }

        return jwtUtils.createToken(user.getId(), user.getAuthority().name(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    @Transactional
    public void signOut(
            UserInfo userInfo
    ) {
    }

    @Transactional
    public void withdraw(
            long userId,
            WithdrawRequestDto withdrawRequestDto
    ) {
        User user = userRepository.findUserById(userId);

        if (!passwordEncoder.matches(withdrawRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(INVALID_PASSWORD);
        }

        if (user.getAuthority().equals(Authority.OWNER)) {
            // TODO 주문이 있는지 확인 필요
            // 주문이 있다면 회원 탈퇴 불가능
            // 주문이 없다면 회원 탈퇴 후 가게 비활성화

            deleteUser(user);
        } else {
            // TODO 주문이 있는지 확인 필요
            // 주문이 있다면 회원 탈퇴 불가능
            // 주문이 없다면 회원 탈퇴
            deleteUser(user);
        }
    }

    private void deleteUser(User user) {
        user.deleteUser();
        userRepository.save(user);
    }
}