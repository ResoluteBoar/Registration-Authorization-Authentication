package com.product.start.security.config;

import com.product.start.security.service.UserService;
import com.product.start.security.storage.UserStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new UserDetailsServiceIml(userService);
    }

    @Bean
    public UserStorage userStorage(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new UserStorage(bCryptPasswordEncoder, new HashMap<>());
    }

    @Bean
    public UserService userService(UserStorage userStorage, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new UserService(bCryptPasswordEncoder, userStorage);
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }
}
