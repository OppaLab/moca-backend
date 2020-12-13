package com.moca.springboot.controller;

import com.moca.springboot.service.AdminService;
import com.moca.springboot.service.CommentService;
import com.moca.springboot.service.MailService;
import com.moca.springboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

//    @RequestMapping("/admin")
//    public String adminPage(Model model) {
//        adminService.adminPage(model);
//        return "index";
//    }

    @DeleteMapping("/deleteAccountByAdmin")
    public void deleteAccountByAdmin(@RequestParam(value = "userId") long userId) throws MessagingException {
        mailService.sendAccountDeleteMail(userId);
    }


    @DeleteMapping("/deletePostByAdmin")
    public long deletePostByAdmin(@RequestParam(value = "postId") long postId) {
        postService.deletePostByAdmin(postId);
        return postId;
    }

    @DeleteMapping("/deleteReviewByAdmin")
    public long deleteReviewByAdmin(@RequestParam(value = "reviewId") long reviewId) {
        postService.deleteReviewByAdmin(reviewId);
        return reviewId;
    }

    @DeleteMapping("/deleteCommentByAdmin")
    public long deleteCommentByAdmin(@RequestParam(value = "commentId") long commentId) {
        commentService.deleteByComment(commentId);
        return commentId;
    }

}
