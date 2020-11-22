package com.moca.springboot.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class PostDTO {
    @Data
    public static class CreatePostRequest {
        private MultipartFile thumbnailImageFile;
        private String thumbnailImageFilePathName;
        private String postTitle;
        private String postBody;
        private long userId;
        private List<String> postCategories;
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
    public static class CreateReviewRequest {
        private long postId;
        private long userId;
        private String review;
    }

    @Data
    public static class GetReviewResponse {
        private long likeCount;
        private long commentCount;
        private Boolean like;
        private long userId;
        private String nickname;
        private String profileImageFilePath;
        private String review;
        private long createdAt;
    }


//    @Data
//    public static class GetPostsInCategoryResponse {
//        private long likeCount;
//        private long commentCount;
//        private Boolean like;
//        private long postId;
//        private long userId;
//        private String nickname;
//        private String profileImageFilePath;
//        private String postTitle;
//        private String postBody;
//        private String thumbnailImageFilePath;
//        private long createdAt;
//        private List<String> categories;
//        private Long reviewId;
//    }
}



