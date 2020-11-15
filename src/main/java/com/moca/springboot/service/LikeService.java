package com.moca.springboot.service;

import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public long createLike(LikeDTO.CreateLikeRequest createLikeRequest) {
        Like like = new Like();
        if (!createLikeRequest.getPostId().isEmpty())
            like.setPost(new Post(Long.parseLong(createLikeRequest.getPostId())));
        if (!createLikeRequest.getReviewId().isEmpty())
            like.setReview(new Review(Long.parseLong(createLikeRequest.getReviewId())));
        like.setUser(new User(createLikeRequest.getUserId()));
        Like newLike = likeRepository.save(like);
        return newLike.getLikeId();
    }

    public long deleteLike(LikeDTO.DeleteLikeRequest deleteLikeRequest) {
        Like like = null;
        if (!deleteLikeRequest.getPostId().isEmpty())
            like = likeRepository.findByUserAndPost(new User(deleteLikeRequest.getUserId()), new Post(Long.parseLong(deleteLikeRequest.getPostId()))).get();
        if (!deleteLikeRequest.getReviewId().isEmpty())
            like = likeRepository.findByUserAndReview(new User(deleteLikeRequest.getUserId()), new Review(Long.parseLong(deleteLikeRequest.getReviewId()))).get();

        likeRepository.delete(like);
        return like.getLikeId();
    }


}
