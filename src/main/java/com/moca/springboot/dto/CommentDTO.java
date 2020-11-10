package com.moca.springboot.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private long postId;
    private long reviewId;
    private long userId;
    private String comment;
}
