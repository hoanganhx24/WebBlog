package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.dto.request.PostUpdateRequest;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.entity.Attachment;
import com.example.webblog.entity.Category;
import com.example.webblog.entity.Post;
import com.example.webblog.enums.PostStatus;
import com.example.webblog.mapper.PostMapper;
import com.example.webblog.repository.PostRepository;
import com.example.webblog.repository.Specification.PostSpecification;
import com.example.webblog.repository.UserRepository;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final PostBusiness postBusiness;

    private final Slugify slugify = new Slugify();

    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           PostMapper postMapper,
                           PostBusiness postBusiness) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.postBusiness = postBusiness;
    }

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        var author = userRepository.findByEmail(postBusiness.getCurrentEmail())
                .orElseThrow(() -> new AuthenticationServiceException("Token invalid")
                );

        List<Category> categories = postBusiness.createListCategory(request.getCategoryIds());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categories(categories)
                .status(PostStatus.PUBLISHED)
                .author(author)
                .slug(slugify.slugify(request.getTitle()))
                .build();
        if (request.getAttachments() != null) {
            List<Attachment> attachments = postBusiness.createListAttachment(request.getAttachments(), post);
            post.setAttachments(attachments);
        }

        return postMapper.toPostResponse(postRepository.save(post));

    }

    @Override
    public void deletePost(String idPost) {
        postBusiness.checkPostAuthor(idPost);
        postRepository.deleteById(idPost);
    }

    @Override
    public PostResponse updatePost(String idPost, PostUpdateRequest request) {
        var post = postRepository.findById(idPost)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postBusiness.checkPostAuthor(idPost);
        if (request.getTitle() != null) {
            String slug = slugify.slugify(request.getTitle());
            post.setTitle(request.getTitle());
            post.setSlug(slug);
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }

        if (request.getCategoryIds() != null) {
            List<Category> categories = postBusiness.createListCategory(request.getCategoryIds());
            post.getCategories().clear();
            post.setCategories(categories);
        }

        if (request.getAttachments() != null) {
            post.getAttachments().clear(); // luôn clear trước nếu có attachments mới gửi lên

            if (!request.getAttachments().isEmpty()) {
                List<Attachment> attachments = postBusiness.createListAttachment(request.getAttachments(), post);
                post.setAttachments(attachments);
            }
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public Page<PostResponse> getPosts(PostFilterRequest request, int page, int pageSize) {
        Sort sort = Sort.unsorted();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Post> spec = PostSpecification.build(request);
        Page<Post> pageResult = postRepository.findAll(spec, pageable);
        List<PostResponse> content = postMapper.toPostResponseList(pageResult.getContent());

        return new PageImpl<>(content, pageable, pageResult.getTotalElements());
    }
}
