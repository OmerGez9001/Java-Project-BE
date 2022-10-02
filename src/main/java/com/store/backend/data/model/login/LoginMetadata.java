package com.store.backend.data.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMetadata {
    @Id
    private String userName;
    private String jwtID;
}
