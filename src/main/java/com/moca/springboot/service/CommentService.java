package com.moca.springboot.service;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.entity.Comment;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public long createComment(CommentDTO.CreateCommentRequest createCommentRequest) {
        Comment comment = new Comment();
        if (!createCommentRequest.getPostId().isEmpty())
            comment.setPost(new Post(Long.parseLong(createCommentRequest.getPostId())));
        if (!createCommentRequest.getReviewId().isEmpty())
            comment.setReview(new Review(Long.parseLong(createCommentRequest.getReviewId())));
        comment.setUser(new User(createCommentRequest.getUserId()));
        comment.setComment(createCommentRequest.getComment());
        Comment newComment = commentRepository.save(comment);
        return newComment.getCommentId();
    }

    public long deleteComment(long commentId, long userId) {

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        if (commentRepository.findById(commentId).get().getUser().getUserId() == userId)
            commentRepository.delete(comment);
        return comment.getCommentId();
    }

    public Page<CommentDTO.GetCommentsOnPostResponse> getCommentsOnPost(long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPost(new Post(postId), pageable);
        Page<CommentDTO.GetCommentsOnPostResponse> getCommentsOnPostResponses = comments.map(comment -> {
            CommentDTO.GetCommentsOnPostResponse getCommentsOnPostResponse = new CommentDTO.GetCommentsOnPostResponse();
            getCommentsOnPostResponse.setUserId(comment.getUser().getUserId());
            getCommentsOnPostResponse.setNickname(comment.getUser().getNickname());
            getCommentsOnPostResponse.setComment(comment.getComment());
            // 댓글 단 시각(몇 분전)을 초단위로 제공
            getCommentsOnPostResponse.setCreatedAt((new Date().getTime() - comment.getCreatedAt().getTime()) / 1000);

            return getCommentsOnPostResponse;
        });
        return getCommentsOnPostResponses;
    }
}
