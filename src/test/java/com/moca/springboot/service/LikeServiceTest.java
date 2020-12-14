package com.moca.springboot.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.moca.springboot.dto.LikeDTO;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LikeServiceTest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;

    private Long userId;
    private Long postId;
    private Long reviewId;

    @Before
    public void setUp() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        userId = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@gmail.com", "1234"));
        postId = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId, Arrays.asList("학업", "연인"), false, null));
        reviewId = reviewService.createReview(new PostDTO.CreateReviewRequest(postId, userId, "행복합니다."));
    }

    @Test
    public void createLike() throws FirebaseMessagingException {
        Long result = likeService.createLike(new LikeDTO.CreateLikeRequest(postId.toString(), "", userId));
        assertNotNull(result);
        result = likeService.createLike(new LikeDTO.CreateLikeRequest("", reviewId.toString(), userId));
        assertNotNull(result);
    }

    @Test
    public void deleteLike() throws FirebaseMessagingException {
        likeService.createLike(new LikeDTO.CreateLikeRequest(postId.toString(), "", userId));
        likeService.createLike(new LikeDTO.CreateLikeRequest("", reviewId.toString(), userId));

        Long result = likeService.deleteLike(postId.toString(), "", userId );
        assertNotNull(result);
        result = likeService.deleteLike("", reviewId.toString(), userId);
        assertNotNull(result);
    }
}