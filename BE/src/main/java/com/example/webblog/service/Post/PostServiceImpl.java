package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.entity.Attachment;
import com.example.webblog.entity.Category;
import com.example.webblog.entity.Post;
import com.example.webblog.entity.PostStatus;
import com.example.webblog.mapper.PostMapper;
import com.example.webblog.repository.AttachmentRepository;
import com.example.webblog.repository.CategoryRepository;
import com.example.webblog.repository.PostRepository;
import com.example.webblog.repository.UserRepository;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private PostMapper postMapper;

    private final Slugify slugify = new Slugify();

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        String slug = slugify.slugify(request.getTitle());
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        var author = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationServiceException("Token invalid")
                );

        List<Category> categories = new ArrayList<>();
        for (String categoryId : request.getCategoryIds()) {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            categories.add(category);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categories(categories)
                .status(PostStatus.PUBLISHED)
                .author(author)
                .slug(slug)
                .build();
        if (request.getAttachments() != null) {
            List<Attachment> attachments = request.getAttachments()
                    .stream()
                    .map(attachment -> Attachment.builder()
                            .url(attachment.getUrl())
                            .type(attachment.getType())
                            .post(post)
                            .build())
                    .toList();
            post.setAttachments(attachments);
        }

        return postMapper.toPostResponse(postRepository.save(post));

    }

    @Override
    public void deletePost(String id) {
        var context =  SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationServiceException("Token invalid"));
        var author = postRepository.findAuthorByPostId(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!user.getId().equals(author.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }
        postRepository.deleteById(id);
    }
}
