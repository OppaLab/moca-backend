package com.moca.springboot.dto;

import lombok.Data;

import java.util.List;

public class UserDTO {
    @Data
    public static class SignUpRequest {
        private String nickname;
        private List<String> userCategoryList;
        private String email;
    }
}
