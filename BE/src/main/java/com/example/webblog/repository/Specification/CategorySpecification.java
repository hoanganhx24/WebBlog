package com.example.webblog.repository.Specification;

import com.example.webblog.dto.request.CategoryFilterRequest;
import com.example.webblog.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> build(CategoryFilterRequest request){
        return hasName(request.getName());
    }

    public static Specification<Category> hasName(String name){
        return (root, query, cb)
                -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
