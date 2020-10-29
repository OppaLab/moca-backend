package com.moca.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LikePost {
    private long like_id;
    private long post_id;
    private long review_id;
    private long user_id;
    private LocalDateTime created_at;

}
