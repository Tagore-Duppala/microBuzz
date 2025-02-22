package com.microBuzz.user_service.service;

import com.microBuzz.user_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        try {
            return Jwts.builder()
                    .subject(user.getId().toString())
                    .claim("email", user.getEmail())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 100))
                    .signWith(getSecretKey())
                    .compact();
        } catch (Exception ex) {
            log.error("Exception occurred in generateAccessToken , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in generateAccessToken: "+ex.getMessage());
        }
    }

    public Long getUserIdFromToken(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.valueOf(claims.getSubject());
        } catch (Exception ex) {
            log.error("Exception occurred in getUserIdFromToken , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getUserIdFromToken: "+ex.getMessage());
        }
    }

}
