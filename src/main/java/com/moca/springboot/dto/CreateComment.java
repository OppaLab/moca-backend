package com.moca.springboot.dto;


import com.moca.springboot.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateComment {
    private long comment_id;
    private long post_id;
    private long review_id;
    private long user_id;
    private String comment_text;
    private LocalDateTime created_at;
}
