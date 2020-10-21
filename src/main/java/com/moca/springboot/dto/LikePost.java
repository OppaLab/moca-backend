package com.moca.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LikePost {
    private int like_id;
    private int post_id;
    private int review_id;
    private int user_id;
//    private LocalDateTime created_at;

}
