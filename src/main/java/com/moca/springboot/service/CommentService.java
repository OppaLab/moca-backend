package com.moca.springboot.service;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private LikeRepository likeRepository;

    public long createComment(CommentDTO.CreateCommentRequest createCommentRequest) {
        Comment comment = new Comment();

        Activity activity = new Activity();
        activity.setUser(new User(createCommentRequest.getUserId()));

        if (!createCommentRequest.getPostId().isEmpty()) {
            comment.setPost(new Post(Long.parseLong(createCommentRequest.getPostId())));
            Post post = postRepository.findById(Long.parseLong(createCommentRequest.getPostId())).get();
            post.setCommentCount(post.getCommentCount() + 1);
            postRepository.save(post);

            activity.setToUser(post.getUser());
            activity.setActivity("comment");
            activity.setPost(post);

        }
        if (!createCommentRequest.getReviewId().isEmpty()) {
            comment.setReview(new Review(Long.parseLong(createCommentRequest.getReviewId())));
            Review review = reviewRepository.findById(Long.parseLong(createCommentRequest.getReviewId())).get();
            activity.setToUser(review.getUser());
            activity.setActivity("comment");
            activity.setReview(review);
        }

        comment.setUser(new User(createCommentRequest.getUserId()));
        comment.setComment(createCommentRequest.getComment());
        Comment newComment = commentRepository.save(comment);
        activity.setComment(newComment);
        activityRepository.save(activity);

        return newComment.getCommentId();
    }

    public long deleteComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getUserId() == userId) {
            activityRepository.deleteAllByComment(comment);
            commentRepository.delete(comment);
        }
        if (comment.getPost() != null) {
            Post post = postRepository.findById(comment.getPost().getPostId()).get();
            post.setCommentCount(post.getCommentCount() - 1);
            postRepository.save(post);
        }
        if (comment.getReview() != null) {

        }

        return comment.getCommentId();
    }

    public Page<CommentDTO.GetCommentsResponse> getComments(String postId, String reviewId, Pageable pageable) {
        Page<Comment> comments = null;
        if (!postId.isEmpty())
            comments = commentRepository.findByPost(new Post(Long.parseLong(postId)), pageable);
        if (!reviewId.isEmpty())
            comments = commentRepository.findByReview(new Review(Long.parseLong(reviewId)), pageable);
        Page<CommentDTO.GetCommentsResponse> getCommentsResponses = comments.map(comment -> {
            CommentDTO.GetCommentsResponse getCommentsResponse = new CommentDTO.GetCommentsResponse();
            getCommentsResponse.setCommentId(comment.getCommentId());
            getCommentsResponse.setUserId(comment.getUser().getUserId());
            getCommentsResponse.setNickname(comment.getUser().getNickname());
            getCommentsResponse.setProfileImageFilePath(comment.getUser().getProfileImageFilePath());
            getCommentsResponse.setComment(comment.getComment());
            // 댓글 단 시각(몇 분전)을 초단위로 제공
            getCommentsResponse.setCreatedAt((new Date().getTime() - comment.getCreatedAt().getTime()) / 1000);

            return getCommentsResponse;
        });
        return getCommentsResponses;
    }

}
