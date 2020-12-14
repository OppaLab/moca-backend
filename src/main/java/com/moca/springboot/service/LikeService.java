package com.moca.springboot.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.ActivityRepository;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FcmService fcmService;

    public Long createLike(LikeDTO.CreateLikeRequest createLikeRequest) throws FirebaseMessagingException {
        Like like = new Like();
        Activity activity = new Activity();
        activity.setUser(new User(createLikeRequest.getUserId()));

        if (!createLikeRequest.getPostId().isEmpty()) {
            like.setPost(new Post(Long.parseLong(createLikeRequest.getPostId())));
            Post post = postRepository.findById(Long.parseLong(createLikeRequest.getPostId())).get();
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);

            activity.setToUser(post.getUser());
            activity.setActivity("like");
            activity.setPost(post);
        }
        if (!createLikeRequest.getReviewId().isEmpty()) {
            like.setReview(new Review(Long.parseLong(createLikeRequest.getReviewId())));
            Review review = reviewRepository.findById(Long.parseLong(createLikeRequest.getReviewId())).get();
            activity.setToUser(review.getUser());
            activity.setActivity("like");
            activity.setReview(review);
        }
        like.setUser(new User(createLikeRequest.getUserId()));
        Like newLike = likeRepository.save(like);
        activityRepository.save(activity);

//        fcmService.sendToToken();

        return newLike.getLikeId();
    }

    public Long deleteLike(String postId, String reviewId, long userId) {
        Like like = null;
        if (!postId.isEmpty()) {
            like = likeRepository.findByUserAndPost(new User(userId), new Post(Long.parseLong(postId))).get();
            Post post = postRepository.findById(like.getPost().getPostId()).get();
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            activityRepository.deleteAllByPost_PostIdAndUser_UserIdAndActivity(Long.parseLong(postId), userId, "like");
        }
        if (!reviewId.isEmpty()) {
            like = likeRepository.findByUserAndReview(new User(userId), new Review(Long.parseLong(reviewId))).get();
            activityRepository.deleteAllByReview_ReviewIdAndUser_UserIdAndActivity(Long.parseLong(reviewId), userId, "like");
        }


        likeRepository.delete(like);
        return like.getLikeId();
    }


}
