package com.moca.springboot.service;

import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;

    public long createLike(LikeDTO.CreateLikeRequest createLikeRequest) {
        Like like = new Like();
        if (!createLikeRequest.getPostId().isEmpty()) {
            like.setPost(new Post(Long.parseLong(createLikeRequest.getPostId())));
            Post post = postRepository.findById(Long.parseLong(createLikeRequest.getPostId())).get();
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
        }
        if (!createLikeRequest.getReviewId().isEmpty())
            like.setReview(new Review(Long.parseLong(createLikeRequest.getReviewId())));
        like.setUser(new User(createLikeRequest.getUserId()));
        Like newLike = likeRepository.save(like);

        return newLike.getLikeId();
    }

    public long deleteLike(String postId, String reviewId, long userId) {
        Like like = null;
        if (!postId.isEmpty()) {
            like = likeRepository.findByUserAndPost(new User(userId), new Post(Long.parseLong(postId))).get();
            Post post = postRepository.findById(like.getPost().getPostId()).get();
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
        }
        if (!reviewId.isEmpty())
            like = likeRepository.findByUserAndReview(new User(userId), new Review(Long.parseLong(reviewId))).get();

        likeRepository.delete(like);
        return like.getLikeId();
    }


}
