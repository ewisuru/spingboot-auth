package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserDetailController {

    @GetMapping("/home")
    public String goToHome(){
        return "<h1>Welcome</h1>";
    }

}
