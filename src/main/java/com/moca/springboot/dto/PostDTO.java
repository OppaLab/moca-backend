package com.moca.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class PostDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreatePostRequest {
        private MultipartFile thumbnailImageFile;
        private String thumbnailImageFilePathName;
        private String postTitle;
        private String postBody;
        private long userId;
        private List<String> postCategories;
        private Boolean isRandomUserPushNotification;
        private Long numberOfRandomUserPushNotification;
    }

    @Data
    public static class GetPostsResponse {
        private long likeCount;
        private long commentCount;
        private Boolean like;
        private long postId;
        private long userId;
        private String nickname;
        private String profileImageFilePath;
        private String postTitle;
        private String postBody;
        private String thumbnailImageFilePath;
        private long createdAt;
        private List<String> categories;
        private Long reviewId;
    }

    @Data
    @AllArgsConstructor
    public static class CreateReviewRequest {
        private long postId;
        private long userId;
        private String review;
    }

    @Data
    public static class GetReviewResponse {
        private long reviewId;
        private long likeCount;
        private long commentCount;
        private Boolean like;
        private long userId;
        private String nickname;
        private String profileImageFilePath;
        private String review;
        private long createdAt;
    }

    @Data
    @AllArgsConstructor
    public static class UpdatePostRequest {
//        private String thumbnailImageFilePathName;
        private String postTitle;
        private String postBody;
        private long userId;
        private List<String> postCategories;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateReviewRequest {
        private String review;
    }
}



