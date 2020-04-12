package com.example.demo.util;

import com.example.demo.model.DBUser;
import com.example.demo.model.SystemUser;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JwtUtilsTest {

    private static final String oldToken = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTg2NTg1ODAyLCJpYXQiOjE1ODY2NzIyMDJ9." +
            "O9pBcAIFvI61vPzS0gNvdjzq2zSt_c_zRAdbPsCj-gQ";

    private SystemUser systemUser() {
        DBUser dbUser = DBUser.builder()
                .id(1)
                .userName("user")
                .password("pwd")
                .active(true)
                .roles("USER,ADMIN")
                .build();
        return new SystemUser(dbUser);
    }

    private SystemUser anotherSystemUser() {
        DBUser dbUser = DBUser.builder()
                .id(1)
                .userName("user2")
                .password("pwd123")
                .active(true)
                .roles("USER")
                .build();
        return new SystemUser(dbUser);
    }

    @Test
    public void createJwtShouldGenerateValidJwt() {
        Assertions.assertTrue(JwtUtils.validateToken(JwtUtils.generateToken(systemUser()), systemUser()));
    }

    @Test
    public void validateTokenShouldSendFalseForDifferentUser(){
        Assertions.assertFalse(JwtUtils.validateToken(JwtUtils.generateToken(systemUser()), anotherSystemUser()));
    }

    @Test
    public void validateTokenShouldThrowExpiredJwtExceptionForOldToken(){
        Assertions.assertThrows(ExpiredJwtException.class, () -> JwtUtils.validateToken(oldToken, systemUser()));
    }

    @Test
    public void extractUserNameShouldReturnUserInJwt() {
        Assertions.assertEquals("user", JwtUtils.extractUserName(JwtUtils.generateToken(systemUser())));
    }

    @Test
    public void extractExpirationShouldReturnFutureDate(){
        Assertions.assertTrue(new Date().before(JwtUtils.extractExpiration(JwtUtils.generateToken(systemUser()))));
    }
}
