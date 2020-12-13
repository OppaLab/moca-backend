package com.moca.springboot.dto;


import lombok.Data;

public class CommentDTO {
    @Data
    public static class CreateCommentRequest {
        private String postId;
        private String reviewId;
        private long userId;
        private String comment;
    }


    @Data
    public static class GetCommentsResponse {
        private long userId;
        private long commentId;
        private String nickname;
        private String profileImageFilePath;
        private String comment;
        private long createdAt;
    }

}

