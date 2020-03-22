package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserDetailController {

    @GetMapping("/home")
    public String goToHome() {
        return "<h1>Welcome</h1>";
    }

    @GetMapping("/user")
    public ResponseEntity user(Principal loggedInUserDetails) {
        return new ResponseEntity(loggedInUserDetails, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity admin(Principal loggedInUserDetails) {
        return new ResponseEntity(loggedInUserDetails, HttpStatus.OK);
    }
}
