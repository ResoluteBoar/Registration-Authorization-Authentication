package com.product.start.security.config;

import com.product.start.security.filter.JWTRequestFilter;
import com.product.start.security.filter.UserAuthenticationFilter;
import com.product.start.security.utils.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public UserAuthenticationFilter userAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        UserAuthenticationFilter filter = new UserAuthenticationFilter(jwtUtils);
        filter.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher.antMatcher(POST, "/api/v1.0/login"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService,
                                           UserAuthenticationFilter filter, JWTUtils jwtUtils) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("api/v1.0/user/register").permitAll()
                        .anyRequest().authenticated())
                .addFilter(filter)
                .addFilterAfter(new JWTRequestFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService);

        return httpSecurity.build();
    }
}
