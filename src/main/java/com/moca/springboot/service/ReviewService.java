package com.moca.springboot.service;

import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    public long createReview(PostDTO.CreateReviewRequest createReviewRequest) {
        Review review = new Review();
        review.setPost(new Post(createReviewRequest.getPostId()));
        review.setUser(new User(createReviewRequest.getUserId()));
        review.setReview(createReviewRequest.getReview());

        return reviewRepository.save(review).getReviewId();
    }


    public PostDTO.GetReviewResponse getReview(long userId, long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        PostDTO.GetReviewResponse getReviewResponse = new PostDTO.GetReviewResponse();
        getReviewResponse.setCommentCount(commentRepository.countByReview(review));
        getReviewResponse.setLikeCount(likeRepository.countByReview(review));
        getReviewResponse.setLike(Boolean.FALSE);
        likeRepository.findByUserAndReview(new User(userId), review).ifPresent(action ->
                getReviewResponse.setLike(Boolean.TRUE));
        getReviewResponse.setCreatedAt((new Date().getTime() - review.getCreatedAt().getTime()) / 1000);
        getReviewResponse.setNickname(review.getUser().getNickname());
        getReviewResponse.setProfileImageFilePath(review.getUser().getProfileImageFilePath());
        getReviewResponse.setReview(review.getReview());
        getReviewResponse.setUserId(review.getUser().getUserId());

        return getReviewResponse;
    }
}
