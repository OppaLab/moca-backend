package com.moca.springboot.repository;

import com.moca.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);
}
