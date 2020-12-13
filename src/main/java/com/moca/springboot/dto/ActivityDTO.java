package com.moca.springboot.dto;

import lombok.Data;

@Data
public class ActivityDTO {

    @Data
    public static class GetActivitiesResponse {
        private long userId;
        private String nickname;
        private String profileImageFilePath;
        private Long postId;
        private Long reviewId;
        /*1. "post": 팔로우한 사람이 고민글 작성
          2. "review": 좋아요한 고민글에 후기가 달림
          3. "like": 내 고민글이나 리뷰에 누군가 좋아요를 함
          4. "follow": 누군가 나를 팔로우함
          5. "comment": 누가 내 고민글이나 리뷰에 댓글을 담
          6. "random": 누군가 나에게 랜덤푸시로 고민글을 보냄*/
        private String activity;
        private String comment;
        private long createdAt;
    }

}
