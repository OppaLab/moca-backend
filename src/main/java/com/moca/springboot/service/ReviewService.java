package com.moca.springboot.service;

import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.ActivityRepository;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public Long createReview(PostDTO.CreateReviewRequest createReviewRequest) {
        Review review = new Review();
        review.setPost(new Post(createReviewRequest.getPostId()));
        review.setUser(new User(createReviewRequest.getUserId()));
        review.setReview(createReviewRequest.getReview());

        Review newReview = reviewRepository.save(review);
        List<Like> likes = likeRepository.findByPost(newReview.getPost());
        for (Like like : likes) {
            Activity activity = new Activity();
            activity.setUser(newReview.getUser());
            activity.setToUser(like.getUser());
            activity.setActivity("review");
            activity.setReview(newReview);
            activityRepository.save(activity);
        }

        return newReview.getReviewId();
    }


    public PostDTO.GetReviewResponse getReview(long userId, long reviewId) {
        Review review = reviewRepository.findById(reviewId).get();
        PostDTO.GetReviewResponse getReviewResponse = new PostDTO.GetReviewResponse();
        getReviewResponse.setReviewId(review.getReviewId());
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
