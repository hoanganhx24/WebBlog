package com.example.webblog.repository.Specification;

import com.example.webblog.dto.request.SearchUserRequest;
import com.example.webblog.entity.Role;
import com.example.webblog.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    public static Specification<User> build(SearchUserRequest request){
        return hasKeyword(request.getKeyword())
                .and(hasRole(request.getRole()))
                .and(hasIsActive(request.getIsActive()))
                .and(hasFromDate(request.getFromDate()))
                .and(hasToDate(request.getToDate()));
    }

    public static Specification<User> hasKeyword(String keyword){
        return ((root, query, cb) -> {
            if (keyword == null ){
                return null;
            }
            String pattern = "%"+keyword.toLowerCase()+"%";
            return cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(cb.concat(cb.coalesce(root.get("firstName"), ""), cb.coalesce(root.get("lastName"), "")), pattern)
            );
        });
    }

    public static Specification<User> hasRole(Role role){
        return (root, query, cb)
                -> role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<User> hasIsActive(Boolean isActive){
        return (root, query, cb)
                -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<User> hasFromDate(LocalDateTime fromDate){
        return (root, query, cb)
                ->  fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }

    public static Specification<User> hasToDate(LocalDateTime toDate){
        return (root, query, cb)
                ->  toDate == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
    }

}
