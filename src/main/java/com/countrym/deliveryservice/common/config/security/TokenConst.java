package com.countrym.deliveryservice.common.config.security;

public class TokenConst {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String USER_ID = "user_id";
    public static final String USER_AUTHORITY = "user_authority";
}