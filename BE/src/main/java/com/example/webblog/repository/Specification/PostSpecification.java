package com.example.webblog.repository.Specification;

import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.entity.Category;
import com.example.webblog.entity.Post;
import com.example.webblog.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {
    public static Specification<Post> build(PostFilterRequest request){
        return hasKeyword(request.getKeyword())
                .and(hasCategory(request.getCategory()))
                .and(hasFromDate(request.getFromDate()))
                .and(hasToDate(request.getToDate()));
    }

    public static Specification<Post> hasKeyword(String keyword){
        return (root, query, cb) -> {
            if(keyword == null){
                return null;
            }
            Join<Post, User> authorJoin = root.join("author");
            String pattern = "%"+keyword.toLowerCase()+"%";
            return cb.or(
                    cb.like(cb.lower(authorJoin.get("username")), pattern),
                    cb.like(cb.concat(cb.coalesce(authorJoin.get("firstName"), ""), cb.coalesce(authorJoin.get("lastName"), "")), pattern),
                    cb.like(cb.lower(root.get("title")), pattern)
            );
        };
    }

    public static Specification<Post> hasCategory(String category){
        return (root, query, cb) -> {
            if(category == null || category.isEmpty()){
                return null;
            }
            String pattern = "%"+category.toLowerCase()+"%";
            Join<Post, Category> categoryJoin = root.join("categories");
            return cb.like(cb.lower(categoryJoin.get("name")), pattern);

        };
    }

    public static Specification<Post> hasFromDate(LocalDateTime fromDate){
        return (root, query, cb)
                ->  fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }

    public static Specification<Post> hasToDate(LocalDateTime toDate){
        return (root, query, cb)
                ->  toDate == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
    }
}
