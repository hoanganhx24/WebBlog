package com.example.webblog.entity;

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

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String username;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String avatar;

    private String nickname;

    private LocalDateTime dob;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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

    @OneToMany(mappedBy = "author")
    private List<Post>  posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public String getFullName() {
        return Stream.of(lastName, firstName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
