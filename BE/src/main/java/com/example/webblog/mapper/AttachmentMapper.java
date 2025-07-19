package com.example.webblog.mapper;

import com.example.webblog.dto.response.AttachmentResponse;
import com.example.webblog.entity.Attachment;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {
    public AttachmentResponse toAttachmentResponse(Attachment attachment) {
        AttachmentResponse attachmentResponse = AttachmentResponse.builder()
                .url(attachment.getUrl())
                .type(attachment.getType())
                .build();
        return attachmentResponse;
    }
}
