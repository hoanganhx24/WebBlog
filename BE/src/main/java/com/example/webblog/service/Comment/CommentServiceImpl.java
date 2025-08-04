package com.example.webblog.service.Comment;

import com.example.webblog.configuaration.CustomUserDetails;
import com.example.webblog.dto.request.CommentCreateRequest;
import com.example.webblog.dto.request.CommentUpdateRequest;
import com.example.webblog.dto.response.CommentResponse;
import com.example.webblog.entity.Comment;
import com.example.webblog.entity.Post;
import com.example.webblog.entity.User;
import com.example.webblog.mapper.CommentMapper;
import com.example.webblog.repository.CommentRepository;
import com.example.webblog.repository.PostRepository;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponse createComment(CommentCreateRequest request) {
        Post post = postRepository.findById(request.getPostId()).orElse(null);
        if (Objects.isNull(post)) {
            throw new EntityNotFoundException("Post Not Found");
        }
        User user = userRepository.findUserById(getCurrentIdUser()).orElse(null);
        if (Objects.isNull(user)) {
            throw new EntityNotFoundException("User not found");
        }
        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public Page<CommentResponse> getComments(String postId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Comment> pageResult = commentRepository.findAll(pageable);
        List<CommentResponse> content = commentMapper.toCommentResponseList(pageResult.getContent());

        return new PageImpl<>(content, pageable, pageResult.getTotalElements());
    }

    @Override
    public CommentResponse updateComment(CommentUpdateRequest request, String idComment) {

        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (Objects.isNull(comment)) {
            throw new EntityNotFoundException("Comment Not Found");
        }

        if (!commentRepository.existsByIdAndUserId(idComment, getCurrentIdUser())) {
            throw new AccessDeniedException("You are not allowed to update this comment");
        }

        comment.setContent(request.getContent());

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(String idComment) {
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (Objects.isNull(comment)) {
            throw new EntityNotFoundException("Comment Not Found");
        }
        if (!commentRepository.existsByIdAndUserId(idComment, getCurrentIdUser())) {
            throw new AccessDeniedException("You are not allowed to delete this comment");
        }
        commentRepository.deleteById(idComment);
    }

    public String getCurrentIdUser() {
        var context = SecurityContextHolder.getContext();
        CustomUserDetails user =(CustomUserDetails) context.getAuthentication().getPrincipal();
        // log.info("Current User: {} {}", user.getUsername(), user.getUserId());
        return user.getUserId();
    }
}
