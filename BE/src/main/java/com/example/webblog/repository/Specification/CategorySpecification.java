package com.example.webblog.repository.Specification;

import com.example.webblog.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> build(String keyword){
        return hasName(keyword);
    }

    public static Specification<Category> hasName(String name){
        return (root, query, cb)
                -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
