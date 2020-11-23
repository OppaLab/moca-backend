package com.moca.springboot.dto;

import lombok.Data;

@Data
public class ActivityDTO {

    @Data
    public static class GetActivitiesResponse {
        private long userId;
        private String nickname;
        private String profileImageFilePath;
        private Long postId;
        private Long reviewId;
        private String activity;
        private String comment;
        private long createdAt;
    }

}
