package com.moca.springboot.service;

import com.moca.springboot.dto.ReportDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.ReportRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;

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
    @Autowired
    private MailService mailService;

    @Async
    public void report(ReportDTO.ReportRequest reportRequest) throws MessagingException {
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

        Report newReport = reportRepository.save(report);

        mailService.sendReportMail(newReport.getReportedUser().getUserId(), newReport.getReportId());
    }


    public Page<ReportDTO.GetReportResponse> getReports(Pageable pageable) {
        Page<Report> reports = reportRepository.findAll(pageable);
        Page<ReportDTO.GetReportResponse> getReportResponses;
        getReportResponses =
                reports.map(report -> {
                    ReportDTO.GetReportResponse getReportResponse = new ReportDTO.GetReportResponse();
                    getReportResponse.setReportId(report.getReportId());
                    getReportResponse.setUserId(report.getUser().getUserId());
                    getReportResponse.setUserNickName(report.getUser().getNickname());
                    getReportResponse.setReportedUserId(report.getReportedUser().getUserId());
                    getReportResponse.setReportedUserNickName(report.getReportedUser().getNickname());
                    if (report.getPost() != null) {
                        getReportResponse.setReportWhat("post");
                        getReportResponse.setPostId(report.getPost().getPostId());
                    } else if (report.getReview() != null) {
                        getReportResponse.setReportWhat("review");
                        getReportResponse.setReviewId(report.getReview().getReviewId());
                    } else if (report.getComment() != null) {
                        getReportResponse.setReportWhat("comment");
                        getReportResponse.setPostId(report.getComment().getPost().getPostId());
                        getReportResponse.setCommentId(report.getComment().getCommentId());
                    }
                    getReportResponse.setReportReason(report.getReportReason());
                    getReportResponse.setCreatedAt((new Date().getTime() - report.getCreatedAt().getTime()) / 1000);

                    return getReportResponse;
                });
        return getReportResponses;
    }
}
