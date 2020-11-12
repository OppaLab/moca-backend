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
    public static class DeleteCommentRequest {
        private long commentId;
        private long userId;
    }

    @Data
    public static class GetCommentsOnPostResponse {
        private long userId;
        private String nickname;
        private String comment;
        private long createdAt;
    }
}

