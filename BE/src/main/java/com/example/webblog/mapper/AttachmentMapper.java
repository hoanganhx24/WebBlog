package com.example.webblog.mapper;

import com.example.webblog.dto.response.AttachmentResponse;
import com.example.webblog.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttachmentMapper {
    AttachmentResponse toAttachmentResponse(Attachment attachment);
}
