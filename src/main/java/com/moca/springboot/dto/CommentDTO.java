package com.moca.springboot.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private long postId;
    private long reviewId;
    private long userId;
    private String text;
    private LocalDateTime createdAt;
}
