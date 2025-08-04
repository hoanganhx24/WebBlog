package com.example.webblog.controller;

import com.example.webblog.dto.request.CommentCreateRequest;
import com.example.webblog.dto.request.CommentUpdateRequest;
import com.example.webblog.dto.response.CommentResponse;
import com.example.webblog.service.Comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentResponse createComment(@RequestBody CommentCreateRequest request){
        return commentService.createComment(request);
    }

    @PutMapping("/{idComment}")
    public CommentResponse updateComment(@PathVariable String idComment, @RequestBody CommentUpdateRequest request){
        return commentService.updateComment(request, idComment);
    }

    @DeleteMapping("{idComment}")
    public void deleteComment(@PathVariable String idComment){
        commentService.deleteComment(idComment);
    }
}
