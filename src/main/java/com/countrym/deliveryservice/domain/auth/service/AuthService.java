package com.countrym.deliveryservice.domain.auth.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.DuplicateElementException;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.common.util.JwtUtils;
import com.countrym.deliveryservice.domain.auth.dto.request.SignInRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.SignUpRequestDto;
import com.countrym.deliveryservice.domain.auth.dto.request.WithdrawRequestDto;
import com.countrym.deliveryservice.domain.auth.facade.AuthFacade;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.enums.Authority;
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
    private final AuthFacade authFacade;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (authFacade.existsEmail(signUpRequestDto.getEmail())) {
            throw new DuplicateElementException(ALREADY_SIGNED_EMAIL_ERROR);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User newUser = User.from(signUpRequestDto, encodedPassword);

        authFacade.saveUser(newUser);
    }

    @Transactional(readOnly = true)
    public String signIn(SignInRequestDto signInRequestDto) {
        User user = authFacade.getUserByEmail(signInRequestDto.getEmail());

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
        User user = authFacade.getUser(userId);

        if (!passwordEncoder.matches(withdrawRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(INVALID_PASSWORD);
        }

        if (user.getAuthority().equals(Authority.OWNER)) {
            // 폐업되지 않은 가게가 있다면 회원 탈퇴 불가능
            if (authFacade.existsOpenedStore(userId)) {
                throw new InvalidParameterException(INVALID_WITHDRAW_OWNER);
            }

            // 폐업되지 않은 가게가 없다면 회원 탈퇴
            authFacade.deleteUser(user);
        } else {
            // 주문이 있다면 회원 탈퇴 불가능
            if (authFacade.existUncompletedOrder(userId)) {
                throw new InvalidParameterException(INVALID_WITHDRAW_USER);
            }

            // 주문이 없다면 회원 탈퇴
            authFacade.deleteUser(user);
        }
    }
}