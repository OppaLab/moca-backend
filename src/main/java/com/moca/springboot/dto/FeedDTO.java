package com.moca.springboot.dto;

import lombok.Data;

import java.util.List;


public class FeedDTO {

    @Data
    public static class GetFeedsAtHomeResponse {
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
    }


}
