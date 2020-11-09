package com.moca.springboot.service;

import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.dto.UnlikeDTO;
import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public long like(LikeDTO likeDTO) {

        Post post = new Post();
        Like like = new Like();

        post.setPostId(likeDTO.getPostId());
        like.setReviewId(likeDTO.getReviewId());
        like.setUserId(likeDTO.getUserId());
        like.setCreatedAt(LocalDateTime.now());
        like.setPost(post);
        Like newLike = likeRepository.save(like);

        return newLike.getLikeId();
    }

    public long unlike(UnlikeDTO unlikeDTO) {

        Like like = new Like();
        like.setLikeId(unlikeDTO.getLike_id());
        likeRepository.delete(like);
        return like.getLikeId();
    }


}
