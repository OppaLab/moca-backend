//package com.moca.springboot.service;
//
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.moca.springboot.SpringbootApplication;
//import com.moca.springboot.dto.PostDTO;
//import com.moca.springboot.dto.ReportDTO;
//import com.moca.springboot.dto.UserDTO;
//import com.moca.springboot.entity.Report;
//import com.moca.springboot.repository.ReportRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.task.SyncTaskExecutor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.mail.MessagingException;
//import javax.transaction.Transactional;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.Executor;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class ReportServiceTest {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private ReportService reportService;
//    @Autowired
//    private ReportRepository reportRepository;
//
//    private Long userId1;
//    private Long userId2;
//    private Long postId;
//
//    @Configuration
//    @Import(SpringbootApplication.class)
//    static class ContextConfiguration {
//        @Bean
//        @Primary
//        public Executor executor() {
//            return new SyncTaskExecutor();
//        }
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        userId1 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@gmail.com", "1234"));
//        userId2 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@ajou.ac.kr", "1234"));
//
//        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
//        postId = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("사랑", "연인"), false, null));
//    }
//
//    @Test
//    public void report() throws MessagingException, IOException, FirebaseMessagingException {
//        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
//        postId = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("사랑", "연인"), false, null));
//
//        reportService.report(new ReportDTO.ReportRequest(userId2, null, postId, null, null, "광고"));
//        List<Report> result = reportRepository.findAll();
//        System.out.println(result);
//        assertNotNull(result);
//    }
//
//    @Test
//    public void getReports() throws MessagingException, IOException, FirebaseMessagingException {
//
//        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
//        postId = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("사랑", "연인"), false, null));
//
//        reportService.report(new ReportDTO.ReportRequest(userId2, null, postId, null, null, "광고"));
//        Page<ReportDTO.GetReportResponse> result = reportService.getReports(PageRequest.of(0, 10));
//        assertThat(result.getContent().get(0).getReportReason(), is("광고"));
//
//    }
//}