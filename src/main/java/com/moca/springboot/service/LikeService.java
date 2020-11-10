package com.moca.springboot.service;

import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.dto.UnlikeDTO;
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

    public long like(LikeDTO likeDTO) {

        Like like = new Like();

        like.setUser(new User(likeDTO.getUserId()));
        like.setReview(new Review(likeDTO.getReviewId()));
        like.setPost(new Post(likeDTO.getPostId()));
        Like newLike = likeRepository.save(like);

        return newLike.getLikeId();
    }

    public long unlike(UnlikeDTO unlikeDTO) {

        Like like = new Like();
        like.setLikeId(unlikeDTO.getLikeId());
        likeRepository.delete(like);
        return like.getLikeId();
    }


}
