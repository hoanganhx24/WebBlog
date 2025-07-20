package com.example.webblog.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content; // Nội dung tin nhắn
    private LocalDateTime sentAt; // Thời gian gửi

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender; // Người gửi

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation; // Cuộc trò chuyện

}