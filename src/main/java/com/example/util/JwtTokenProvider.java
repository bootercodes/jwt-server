package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * Utility for generating, validating and parsing the JWT token.
 */
@Slf4j
@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    // TODO -- HACKY -- Should come from the secure env variables
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final JwtParser JWT_PARSER = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build();

    /**
     * Expiration time of the newly generated token (time-to-live)
     */
    private final long TTL = 120_000; // 2 Minutes

    /**
     * Generates new JWT for the given authenticated user.
     *
     * @param userDetails User details.
     * @return JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(TTL)))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .compact();
    }

    /**
     * Validates the token provided in the API request.
     *
     * @param token JWT token getting validate.
     * @return True if token is valid otherwise false.
     */
    public boolean validateToken(String token) {
        try {
            JWT_PARSER.parse(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT ", e);
            return false;
        }
    }

    /**
     * Returns the Claims body for the validated JWT token.
     *
     * @param token Validated JWT token.
     * @return Claims body from the parsed JWT.
     */
    public Claims getJwtBody(String token) {
        return (Claims) ((DefaultJws<?>) JWT_PARSER.parse(token))
                .getBody();
    }
}
