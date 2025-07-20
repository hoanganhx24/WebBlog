package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.dto.request.PostUpdateRequest;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.dto.response.PostFilterResponse;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.Attachment;
import com.example.webblog.entity.Category;
import com.example.webblog.entity.Post;
import com.example.webblog.enums.PostStatus;
import com.example.webblog.mapper.PostMapper;
import com.example.webblog.repository.AttachmentRepository;
import com.example.webblog.repository.CategoryRepository;
import com.example.webblog.repository.PostRepository;
import com.example.webblog.repository.Specification.PostSpecification;
import com.example.webblog.repository.UserRepository;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

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

    @Autowired
    private PostBusiness postBusiness;

    private final Slugify slugify = new Slugify();

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
            if (request.getAttachments().size() > 0) {
                List<Attachment> attachments = postBusiness.createListAttachment(request.getAttachments(), post);
                post.getAttachments().clear();
                post.setAttachments(attachments);
            }
            else if (request.getAttachments().size() == 0) {
                post.getAttachments().clear();
            }
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public PageResponse<PostFilterResponse> getPosts(PostFilterRequest request, int page, int pageSize) {
        Sort sort = Sort.unsorted();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<Post> spec = PostSpecification.build(request);
        Page<Post> pageResult = postRepository.findAll(spec, pageable);
        List<PostFilterResponse> content = pageResult.getContent()
                .stream()
                .map(post -> postMapper.toPostFilterResponse(post))
                .toList();

        return PageResponse.<PostFilterResponse>builder()
                .page(page)
                .size(pageSize)
                .content(content)
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }
}
