package com.lastcalleats.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.expiration-ms:86400000}")
  private long expirationMs;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Long userId, String role) {
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("role", role)
        .issuedAt(now)
        .expiration(expireDate)
        .signWith(secretKey)
        .compact();
  }

  public Long extractUserId(String token) {
    Claims claims = extractAllClaims(token);
    return Long.valueOf(claims.getSubject());
  }

  public String extractRole(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("role", String.class);
  }

  public boolean validateToken(String token) {
    try {
      Claims claims = extractAllClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}