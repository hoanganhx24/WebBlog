package com.example.webblog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String username;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String fullname;

    @Column(name = "isactive")
    private Integer isActive;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String avatar;

    private String nickname;

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

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,  orphanRemoval = true)
//    private Author author;
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Admin admin;
}
