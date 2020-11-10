package com.moca.springboot.dto.requestDto;

import lombok.Data;

@Data
public class ReviewDTO {
    private long postId;
    private long userId;
    private String review;
}
