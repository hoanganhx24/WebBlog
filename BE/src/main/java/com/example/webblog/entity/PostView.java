package com.example.webblog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PostView {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    private LocalDateTime viewAt;

}
