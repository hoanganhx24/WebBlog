package com.example.webblog.entity;

import com.example.webblog.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String avatar;

    @Column(unique = true)
    private String nickname;

    private LocalDateTime dob;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "author",  fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post>  posts;

    @OneToMany(mappedBy = "user",   fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reaction> reactions;

}
