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
    }

    @Data
    public static class SetProfileImageRequest {
        private long userId;
        private MultipartFile profileImageFile;
    }
}
