package com.example.demo.controller;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class UserDetailController {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtils jwtUtils;

    @GetMapping("/home")
    public String goToHome() {
        return "<h1>Welcome</h1>";
    }

    @GetMapping("/user")
    public ResponseEntity user(Principal loggedInUserDetails) {
        return ResponseEntity.ok(loggedInUserDetails);
    }

    @GetMapping("/admin")
    public ResponseEntity admin(Principal loggedInUserDetails) {
        return ResponseEntity.ok(loggedInUserDetails);
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            System.out.println(ex);
            throw ex;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
