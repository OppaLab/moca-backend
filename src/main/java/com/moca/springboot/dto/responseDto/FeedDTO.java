package com.moca.springboot.dto.responseDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FeedDTO {
    private long likeCount;
    private long commentCount;
    private Boolean like;
    private long postId;
    private long userId;
    private String nickname;
    private String postTitle;
    private String postBody;
    private String thumbnailImageFilePath;
    private long createdAt;
}
