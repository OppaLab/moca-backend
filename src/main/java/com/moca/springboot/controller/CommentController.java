package com.moca.springboot.controller;

import com.moca.springboot.dto.CreateComment;
import com.moca.springboot.dto.DeleteComment;
import com.moca.springboot.model.Comment;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class CommentController
{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comment/1")
    public @ResponseBody
    Long createComment (CreateComment createComment) {
        Long comment_id = commentService.createComment(createComment);
        return comment_id;
    }

    @GetMapping("/api/all-comment")
    public @ResponseBody
    Iterable<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @DeleteMapping("/api/comment/2")
    public @ResponseBody
    Long deleteComment(DeleteComment deleteComment) {
        Long comment_id = commentService.deleteComment(deleteComment);
        return comment_id;
    }

}