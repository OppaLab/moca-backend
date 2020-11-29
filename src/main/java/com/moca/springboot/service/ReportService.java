package com.moca.springboot.service;

import com.moca.springboot.dto.ReportDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.ReportRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommentRepository commentRepository;

    public long report(ReportDTO.ReportRequest reportRequest) {
        Report report = new Report();
        report.setUser(new User(reportRequest.getUserId()));
        report.setReportReason(reportRequest.getReportReason());

        // 계정 신고
        if (reportRequest.getReportedUserId() != null) {
            report.setReportedUser(new User(reportRequest.getReportedUserId()));
        }
        // 고민글 신고
        if (reportRequest.getPostId() != null) {
            report.setReportedUser(new User(postRepository.findById(reportRequest.getPostId()).get().getUser().getUserId()));
            report.setPost(new Post(reportRequest.getPostId()));
        }
        // 후기글 신고
        if (reportRequest.getReviewId() != null) {
            report.setReportedUser(new User(reviewRepository.findById(reportRequest.getReviewId()).get().getUser().getUserId()));
            report.setReview(new Review(reportRequest.getReviewId()));
        }
        // 댓글 신고
        if (reportRequest.getCommentId() != null) {
            report.setReportedUser(new User(commentRepository.findById(reportRequest.getCommentId()).get().getUser().getUserId()));
            report.setComment(new Comment(reportRequest.getCommentId()));
        }

        return reportRepository.save(report).getReportId();
    }
}
