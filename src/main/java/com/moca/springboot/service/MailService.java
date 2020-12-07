package com.moca.springboot.service;

import com.moca.springboot.entity.Report;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.ReportRepository;
import com.moca.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Async
    public void sendReportMail(long reportedUserId, long reportId) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        User reportedUser = userRepository.findById(reportedUserId).get();
        Report report = reportRepository.findById(reportId).get();
        helper.setSubject("MOCA 운영팀 알림: " + reportedUser.getNickname() + "님이 신고 되었습니다.");
        helper.setTo(reportedUser.getEmail());


        Context context = new Context();
        context.setVariable("createdAt", report.getCreatedAt());
        if (report.getPost() != null) {
            context.setVariable("reportWhat", "고민글");
            context.setVariable("reportedContent", report.getPost().getPostTitle());
        } else if (report.getReview() != null) {
            context.setVariable("reportWhat", "후기");
            context.setVariable("reportedContent", report.getReview().getReview());
        } else if (report.getComment() != null) {
            context.setVariable("reportWhat", "댓글");
            context.setVariable("reportedContent", report.getComment().getComment());
        }

        String html = templateEngine.process("report-mail-template", context);
        helper.setText(html, true);


        javaMailSender.send(message);
    }

    @Async
    public void sendAccountDeleteMail(long userId) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        User user = userRepository.findById(userId).get();

        helper.setSubject("MOCA 운영팀 알림: " + user.getNickname() + "님의 계정이 삭제조치 되었습니다.");
        helper.setTo(user.getEmail());

        Context context = new Context();

        String html = templateEngine.process("delete-account-mail-template", context);
        helper.setText(html, true);

        javaMailSender.send(message);

        userService.signOut(userId);
    }
}
