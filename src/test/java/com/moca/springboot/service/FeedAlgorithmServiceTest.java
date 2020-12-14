package com.moca.springboot.service;

import com.moca.springboot.dto.FeedDTO;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FeedAlgorithmServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private FeedAlgorithmService feedAlgorithmService;
    @Autowired
    private FeedService feedService;

    private Long userId1;
    private Long userId2;
    private Long postId1;
    private Long postId2;
    private Long postId3;


    @Before
    public void setUp() throws Exception {
        userId1 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑, 연"), "jaewan9074@gmail.com", "1234"));
        userId2 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@ajou.ac.kr", "1234"));

        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        postId1 = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId2, Arrays.asList("사랑", "연인"), false, null));
        multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        postId2 = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId2, Arrays.asList("사랑", "학업"), false, null));
        multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        postId3 = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId2, Arrays.asList("가", "직장"), false, null));
    }

    @Test
    public void runScheduledFeedAlgorithm() {
        feedAlgorithmService.runScheduledFeedAlgorithm();
        Page<FeedDTO.GetFeedsAtHomeResponse> result = feedService.getFeedsAtHome(userId1, PageRequest.of(0, 10));
        assertThat(result.getContent().size(), is(3));
        assertThat(result.getContent().get(0).getPostId(), is(postId1));
        assertThat(result.getContent().get(1).getPostId(), is(postId2));
        assertThat(result.getContent().get(2).getPostId(), is(postId3));
    }

    @Test
    public void runFeedAlgorithmForNewUser() {
        feedAlgorithmService.runScheduledFeedAlgorithm();
        Long newUserId = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑, 연"), "jaewan9074@naver.com", "1234"));
        Page<FeedDTO.GetFeedsAtHomeResponse> result = feedService.getFeedsAtHome(newUserId, PageRequest.of(0, 10));
        assertThat(result.getContent().size(), is(3));
        assertThat(result.getContent().get(0).getPostId(), is(postId1));
        assertThat(result.getContent().get(1).getPostId(), is(postId2));
        assertThat(result.getContent().get(2).getPostId(), is(postId3));
    }
}