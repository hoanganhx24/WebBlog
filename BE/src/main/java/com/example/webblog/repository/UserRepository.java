package com.example.webblog.repository;

import com.example.webblog.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsById(String id);
    boolean existsByEmail(String email);
    Optional<User> findUserById(String id);
    Optional<User> findByEmail(String email);
}
