package com.moca.springboot.service;

import com.moca.springboot.dto.requestDto.ReviewDTO;
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

    public long addReview(ReviewDTO reviewDTO) {

        Review review = new Review();
        review.setPost(new Post(reviewDTO.getPostId()));
        review.setUser(new User(reviewDTO.getUserId()));
        review.setReview(reviewDTO.getReview());

        return reviewRepository.save(review).getReview_id();
    }
}
