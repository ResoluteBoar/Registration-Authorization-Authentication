package com.product.start.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.start.security.dto.LoginPasswordDTO;
import com.product.start.security.entity.UserDetailsEntity;
import com.product.start.security.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String username = "";
        String password = "";

        try (ServletInputStream inputStream = request.getInputStream()) {
            LoginPasswordDTO loginPasswordDTO = new ObjectMapper().readValue(inputStream, LoginPasswordDTO.class);
            username = loginPasswordDTO.getUsername();
            password = loginPasswordDTO.getPassword();
        } catch (IOException ex) {
            log.error("Invalid login request", ex);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager()
                .authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        UserDetailsEntity user = (UserDetailsEntity) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(user);

        Map<String, String> token = new HashMap<>();

        token.put("access_token", accessToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();

        error.put("error_message", failed.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

}
