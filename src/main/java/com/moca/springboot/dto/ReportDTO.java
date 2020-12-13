package com.moca.springboot.dto;

import lombok.Data;

public class ReportDTO {

    @Data
    public static class ReportRequest {
        // 나의 userId
        private Long userId;
        // 신고하는 계정의 userId (계정신고가 아니면 null)
        private Long reportedUserId;
        // 신고하는 고민글의 postId (고민글신고가 아니면 null)
        private Long postId;
        // 신고하는 후기글의 postId (후기글신고가 아니면 null)
        private Long reviewId;
        // 신고하는 댓글의 commentId (댓글 신고가 아니면 null)
        private Long commentId;
        // 신고사유: 비방, 욕설, 광고, ...(어느 종류의 신고든 항상 들어가야함)
        private String reportReason;
    }

    @Data
    public static class GetReportResponse {
        private Long reportId;
        // 신고한 사람의 userId
        private Long userId;
        // 신고한 사람의 닉네임
        private String userNickName;
        // 신고당한 사람의 userId
        private Long reportedUserId;
        // 신고당한 사람의 닉네임
        private String reportedUserNickName;
        /* reportWhat: 신고한 대상이 고민글 or 후기글 or 댓글 인지 알려줌
        고민글 = "post"
        후기글 = "review"
        댓글 = "comment"
         */
        private String reportWhat;
        private Long postId;
        private Long reviewId;
        private Long commentId;
        private String reportReason;
        private Long createdAt;
    }
}
