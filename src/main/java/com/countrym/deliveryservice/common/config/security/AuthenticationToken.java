package com.countrym.deliveryservice.common.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private final UserInfo userInfo;

    public AuthenticationToken(UserInfo userInfo) {
        super(userInfo.getAuthorities());
        this.userInfo = userInfo;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userInfo;
    }
}