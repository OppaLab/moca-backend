package com.moca.springboot.dto;

import lombok.Data;

@Data
public class LikeDTO {

    @Data
    public static class CreateLikeRequest {
        private String postId;
        private String reviewId;
        private long userId;
    }

    @Data
    public static class DeleteLikeRequest {
        private long postId;
        private long reviewId;
        private long userId;
    }

}
