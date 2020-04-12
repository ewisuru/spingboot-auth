package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {
    private static final String SECRET_KEY = "secret";

    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public static String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userDetails.getUsername().equals(userName);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(LocalDateTime.now()
                        .plusDays(1)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
