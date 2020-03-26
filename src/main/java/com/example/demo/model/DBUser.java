package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_info", schema = "springsecurity")
@Data
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
