package com.example.demo.repository;

import com.example.demo.model.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Long> {
    Optional<DBUser> findByUserName(String userName);
}
