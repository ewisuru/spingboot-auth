package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Table(name = "user_info", schema = "springsecurity")
@AllArgsConstructor
@NoArgsConstructor
public class DBUser {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "user_name")
    private String userName;
    private String password;
    private boolean active;
    private String roles;

}
