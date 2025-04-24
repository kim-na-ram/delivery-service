package com.countrym.deliveryservice.common.config.security;

import com.countrym.deliveryservice.domain.user.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo {
    private Long id;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserInfo of(Long id, Authority authority) {
        return new UserInfo(id, List.of(new SimpleGrantedAuthority(authority.name())));
    }
}