package com.example.webblog.service.Post;

import com.example.webblog.dto.request.AttachmentRequest;
import com.example.webblog.entity.Attachment;
import com.example.webblog.entity.Category;
import com.example.webblog.entity.Post;
import com.example.webblog.repository.CategoryRepository;
import com.example.webblog.repository.PostRepository;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostBusiness {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void checkPostAuthor(String idPost){
        String username = getCurrentUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationServiceException("Token invalid"));
        var author = postRepository.findAuthorByPostId(idPost)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!user.getId().equals(author.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }
    }

    public String getCurrentUsername() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return username;
    }

    public List<Category> createListCategory(List<String> categoryIds) {
        List<Category> categories = new ArrayList<>();
        for (String categoryId : categoryIds) {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            categories.add(category);
        }
        return categories;
    }

    public List<Attachment>  createListAttachment(List<AttachmentRequest> attachmentRequests, Post post) {
        return  attachmentRequests
                .stream()
                .map(attachment -> Attachment.builder()
                        .url(attachment.getUrl())
                        .type(attachment.getType())
                        .post(post)
                        .build())
                .toList();
    }
}
