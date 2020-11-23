package com.moca.springboot.controller;

import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public Long likePost(LikeDTO.CreateLikeRequest createLikeRequest) {
        Long likeId = likeService.createLike(createLikeRequest);
        return likeId;
    }

    @DeleteMapping("/unlike")
    public Long unlikePost(@RequestParam(value = "postId") String postId, @RequestParam(value = "reviewId") String reviewId, @RequestParam(value = "userId") long userId) {
        Long likeId = likeService.deleteLike(postId, reviewId, userId);
        return likeId;
    }

}
