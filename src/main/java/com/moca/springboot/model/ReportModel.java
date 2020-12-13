package com.moca.springboot.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReportModel {
    private Date createdAt;
    private Long userId;
    private Long reportedUserId;
    private Long postId;
    private Long reviewId;
    private Long commentId;
    private String reportReason;
    private String postTitle;
    private String postBody;
    private String comment;
    private String review;
}
