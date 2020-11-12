package com.moca.springboot.service;

import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    public long createReview(PostDTO.CreateReviewRequest createReviewRequest) {
        Review review = new Review();
        review.setPost(new Post(createReviewRequest.getPostId()));
        review.setUser(new User(createReviewRequest.getUserId()));
        review.setReview(createReviewRequest.getReview());

        return reviewRepository.save(review).getReviewId();
    }
}
