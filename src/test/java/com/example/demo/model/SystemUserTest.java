package com.example.demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class SystemUserTest {

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

    @Test
    public void userNameShouldBeTakenFromDbUser() {
        Assertions.assertEquals("user", systemUser().getUsername());
    }

    @Test
    public void passwordShouldBeTakenFromDbUser() {
        Assertions.assertEquals("pwd", systemUser().getPassword());
    }

    @Test
    public void twoAuthoritiesShouldBeTakenFromDbUser() {
        Assertions.assertEquals(2, systemUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).distinct().count());
    }

    @Test
    public void accountNeverExpires() {
        Assertions.assertTrue(systemUser().isAccountNonExpired());
    }

    @Test
    public void accountNeverLocked() {
        Assertions.assertTrue(systemUser().isAccountNonLocked());
    }

    @Test
    public void credentialNeverExpires() {
        Assertions.assertTrue(systemUser().isCredentialsNonExpired());
    }

    @Test
    public void enabledShouldBeTakenFromDbUser() {
        Assertions.assertTrue(systemUser().isEnabled());
    }
}
