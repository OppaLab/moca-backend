package com.moca.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LikeDTO {

    @Data
    @AllArgsConstructor
    public static class CreateLikeRequest {
        private String postId;
        private String reviewId;
        private long userId;
    }


}
