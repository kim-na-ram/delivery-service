package com.countrym.deliveryservice.domain.data.user;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.user.enums.Authority;

public class UserMockData {
    public static UserInfo getUserInfo() {
        return UserInfo.of(1L, Authority.USER);
    }

    public static UserInfo getOwnerInfo() {
        return UserInfo.of(2L, Authority.OWNER);
    }
}
