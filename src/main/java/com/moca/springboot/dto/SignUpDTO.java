package com.moca.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpDTO {
    private String nickname;
    private List<String> userCategoryList;
    private String email;
}
