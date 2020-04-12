package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class AuthenticationRequest {
    String userName;
    String password;
}
