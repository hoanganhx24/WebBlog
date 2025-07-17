package com.example.webblog.repository;

import com.example.webblog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findById(String id);
    Boolean existsBySlug(String slug);
}
