package com.store.backend.config.filter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserWithClaims extends User {
    private Long shopId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }


    public UserWithClaims(String username, String password, Collection<? extends GrantedAuthority> authorities, Long shopId) {
        super(username, password, authorities);
        this.shopId = shopId;
    }
}
