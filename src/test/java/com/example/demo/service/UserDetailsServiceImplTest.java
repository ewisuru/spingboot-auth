package com.example.demo.service;

import com.example.demo.model.DBUser;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    public void loadUserByNameWithValidUserTest() {
        DBUser dbUser = DBUser.builder().id(12).userName("user123").password("sfewigi").active(true).roles("USER,ADMIN").build();
        when(userRepository.findByUserName("user123")).thenReturn(Optional.of(dbUser));
        UserDetails user = userDetailsService.loadUserByUsername("user123");
        Assert.isTrue(user.getUsername().equals("user123"));
        Assert.isTrue(user.getPassword().equals("sfewigi"));
        Assert.isTrue(user.isAccountNonLocked());
        verify(userRepository, times(1)).findByUserName("user123");
    }

    @Test
    public void loadUserByNameThrowsExceptionWhenUserNotFound() {
        when(userRepository.findByUserName("user234")).thenReturn(Optional.empty());
        assertThatExceptionOfType(UsernameNotFoundException.class).
                isThrownBy(() -> userDetailsService.loadUserByUsername("user234"));

    }
}
