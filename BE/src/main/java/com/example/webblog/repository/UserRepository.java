package com.example.webblog.repository;

import com.example.webblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findUserById(String id);
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
}
