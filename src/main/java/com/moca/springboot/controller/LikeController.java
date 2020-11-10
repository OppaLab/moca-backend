package com.moca.springboot.controller;

import com.moca.springboot.dto.requestDto.LikeDTO;
import com.moca.springboot.dto.requestDto.UnlikeDTO;
import com.moca.springboot.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public @ResponseBody
    Long likePost(LikeDTO likeDTO) {
        Long like_id = likeService.like(likeDTO);
        return like_id;
    }

    @DeleteMapping("/unlike")
    public @ResponseBody
    Long unlikePost(UnlikeDTO unlikeDTO) {
        Long like_id = likeService.unlike(unlikeDTO);
        return like_id;
    }

}
