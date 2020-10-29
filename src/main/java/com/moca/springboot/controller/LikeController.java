package com.moca.springboot.controller;

import com.moca.springboot.dto.LikePost;
import com.moca.springboot.dto.UnlikePost;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeService likeService;

    @PostMapping("/api/like")
    public @ResponseBody
    Long likePost (LikePost likePost) {
        Long like_id = likeService.likePost(likePost);
        return like_id;
    }

    @DeleteMapping("/api/unlike")
    public @ResponseBody
    Long unlikePost (UnlikePost unlikePost) {
        Long like_id = likeService.unlikePost(unlikePost);
        return like_id;
    }

}
