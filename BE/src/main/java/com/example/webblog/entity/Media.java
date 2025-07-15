package com.example.webblog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String url;
    private String altText;
    private String type;
    private Boolean isThumbnail;
    private Integer position;
}
