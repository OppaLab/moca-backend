package com.moca.springboot.service;

import com.moca.springboot.model.ReportModel;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.ReportRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    CommentRepository commentRepository;

    public void adminPage(Model model) {
        List<ReportModel> postReportList = new ArrayList<>();
        List<ReportModel> reviewReportList = new ArrayList<>();
        List<ReportModel> commentReportList = new ArrayList<>();
        List<ReportModel> userReportList = new ArrayList<>();

        reportRepository.findByPostNotNullOrderByCreatedAtDesc().forEach(report -> {
            ReportModel reportModel = new ReportModel();
            reportModel.setCreatedAt(report.getCreatedAt());
            reportModel.setUserId(report.getUser().getUserId());
            reportModel.setReportedUserId(report.getReportedUser().getUserId());
            reportModel.setPostId(report.getPost().getPostId());
            reportModel.setReportReason(report.getReportReason());
            reportModel.setPostTitle(report.getPost().getPostTitle());
            reportModel.setPostBody(report.getPost().getPostBody());
            postReportList.add(reportModel);
        });

        reportRepository.findByReviewNotNullOrderByCreatedAtDesc().forEach(report -> {
            ReportModel reportModel = new ReportModel();
            reportModel.setCreatedAt(report.getCreatedAt());
            reportModel.setUserId(report.getUser().getUserId());
            reportModel.setReportedUserId(report.getReportedUser().getUserId());
            reportModel.setReviewId(report.getReview().getReviewId());
            reportModel.setReportReason(report.getReportReason());
            reportModel.setReview(report.getReview().getReview());
            reviewReportList.add(reportModel);
        });

        reportRepository.findByCommentNotNullOrderByCreatedAtDesc().forEach(report -> {
            ReportModel reportModel = new ReportModel();
            reportModel.setCreatedAt(report.getCreatedAt());
            reportModel.setUserId(report.getUser().getUserId());
            reportModel.setReportedUserId(report.getReportedUser().getUserId());
            reportModel.setCommentId(report.getComment().getCommentId());
            reportModel.setReportReason(report.getReportReason());
            reportModel.setComment(report.getComment().getComment());
            commentReportList.add(reportModel);

        });

        reportRepository.findByPostIsNullAndReviewIsNullAndCommentIsNullOrderByCreatedAtDesc().forEach(report -> {
            ReportModel reportModel = new ReportModel();
            reportModel.setCreatedAt(report.getCreatedAt());
            reportModel.setUserId(report.getUser().getUserId());
            reportModel.setReportedUserId(report.getReportedUser().getUserId());
            reportModel.setReportReason(report.getReportReason());

            userReportList.add(reportModel);

        });


        model.addAttribute("postReportList", postReportList);
        model.addAttribute("reviewReportList", reviewReportList);
        model.addAttribute("commentReportList", commentReportList);
        model.addAttribute("userReportList", userReportList);

        System.out.println(postReportList);
    }
}
