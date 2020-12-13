package com.moca.springboot.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserDTO {
    @Data
    public static class SignUpRequest {
        private String nickname;
        private List<String> userCategoryList;
        private String email;
        private String registrationToken;
    }

    @Data
    public static class SetProfileImageRequest {
        private long userId;
        private MultipartFile profileImageFile;
    }

    @Data
    public static class SignInRequest {
        private String email;
    }

    @Data
    public static class FollowRequest {
        private long userId;
        private long followedUserId;
    }

    @Data
    public static class GetProfileResponse {
        private String nickname;
        private String profileImageFilePath;
        private long numberOfPosts;
        private long numberOfFollowers;
        private long numberOfFollowings;
        private Boolean subscribeToPushNotification;
        private Boolean IsFollowed;
        private List<String> userCategories;
        private List<String> userEntities;
    }

    @Data
    public static class UpdateSubscribeRequest {
        private long userId;
    }

    @Data
    public static class UpdateProfileRequest {
        private String nickname;
        private List<String> userCategories;
        private Boolean subscribeToPushNotification;
    }
}
