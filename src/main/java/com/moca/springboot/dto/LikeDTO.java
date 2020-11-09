package com.moca.springboot.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private long postId;
    private long reviewId;
    private long userId;

}
