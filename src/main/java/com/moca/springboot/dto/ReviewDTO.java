package com.moca.springboot.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private long postId;
    private long userId;
    private String review;
}
