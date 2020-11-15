package com.moca.springboot.controller;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public long createComment(CommentDTO.CreateCommentRequest createCommentRequest) {
        return commentService.createComment(createCommentRequest);
    }

    @GetMapping("/comment")
    public Page<CommentDTO.GetCommentsResponse> getComments(@RequestParam(value = "postId") String postId, @RequestParam(value = "reviewId") String reviewId,
                                                            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return commentService.getComments(postId, reviewId, pageable);
    }

    @DeleteMapping("/comment")
    public long deleteComment(@RequestParam(value = "commentId") long commentId, @RequestParam(value = "userId") long userId) {
        return commentService.deleteComment(commentId, userId);
    }

}