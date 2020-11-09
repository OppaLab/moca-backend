package com.moca.springboot.controller;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.dto.DeleteComment;
import com.moca.springboot.entity.Comment;
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

    @PostMapping("/comment")
    public @ResponseBody
    Long createComment(CommentDTO commentDTO) {
        Long comment_id = commentService.createComment(commentDTO);
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