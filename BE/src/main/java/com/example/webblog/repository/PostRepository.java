package com.example.webblog.repository;

import com.example.webblog.entity.Post;
import com.example.webblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,String>, JpaSpecificationExecutor<Post> {
    @Query("SELECT p.author FROM Post p WHERE p.id = :postId")
    Optional<User> findAuthorByPostId(@Param("postId") String postId);
}
