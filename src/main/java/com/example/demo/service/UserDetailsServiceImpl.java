package com.example.demo.service;

import com.example.demo.model.DBUser;
import com.example.demo.model.SystemUser;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<DBUser> dbUser = userRepository.findByUserName(s);
        dbUser.orElseThrow(() -> new UsernameNotFoundException("Could not find a user for name: " + s));
        SystemUser systemUser = dbUser.map(SystemUser::new).get();
        return systemUser;
    }
}
