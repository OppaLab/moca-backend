package com.moca.springboot.service;

import com.moca.springboot.dto.CreateComment;
import com.moca.springboot.dto.LikePost;
import com.moca.springboot.dto.UnlikePost;
import com.moca.springboot.model.Comment;
import com.moca.springboot.model.Like;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public long likePost(LikePost likePost) {

        Like like = new Like();
        like.setLike_id(likePost.getLike_id());
        like.setPost_id(likePost.getPost_id());
        like.setReview_id(likePost.getReview_id());
        like.setUser_id(likePost.getUser_id());
//        like.setCreated_at(LocalDateTime.now());

        Like newLike = likeRepository.save(like);

        return newLike.getLike_id();
    }

    public long unlikePost(UnlikePost unlikePost) {

        Like like = new Like();
        like.setLike_id(unlikePost.getLike_id());
        likeRepository.delete(like);
        return like.getLike_id();
    }


}
