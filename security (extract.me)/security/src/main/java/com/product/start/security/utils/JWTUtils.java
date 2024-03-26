package com.product.start.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.product.start.security.entity.UserDetailsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JWTUtils {

    @Value("{app.jwt.secret-key}")
    private String secret;

    @Value("${app.jwt.expiration-time.access-token}")
    private Duration accessExpiredTime;

    private static final String PERMISSIONS_CLAIMS = "permissions";

    private final Clock clock;


    public String generateAccessToken(UserDetailsEntity user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now(clock).plus(accessExpiredTime))
                .withClaim(PERMISSIONS_CLAIMS,
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(Algorithm.HMAC256(secret));
    }

    public PreAuthenticatedAuthenticationToken getAuthentication(String token) {
        UserDetailsEntity user = extractUserDetails(token);
        return new PreAuthenticatedAuthenticationToken(user, null, user.getAuthorities());
    }

    private DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret.getBytes())).build();
        return verifier.verify(token);
    }

    private UserDetailsEntity extractUserDetails(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        String username = decodedJWT.getSubject();

        if (decodedJWT.getClaim(PERMISSIONS_CLAIMS).isMissing()) {
            throw new JWTVerificationException("invalid token");
        }

        String[] permissions = decodedJWT.getClaim(PERMISSIONS_CLAIMS).asArray(String.class);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Arrays.stream(permissions).forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));

        return new UserDetailsEntity(username, null, new HashSet<>(authorities));
    }


}
