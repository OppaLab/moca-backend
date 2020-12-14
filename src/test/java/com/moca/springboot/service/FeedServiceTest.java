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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FeedServiceTest {
    @Autowired
    private FeedService feedService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private FeedAlgorithmService feedAlgorithmService;

    private Long userId1;
    private Long userId2;
    private Long postId1;
    private Long postId2;

    @Before
    public void setUp() throws Exception {
        userId1 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@gmail.com", "1234"));
        userId2 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@ajou.ac.kr", "1234"));

        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        postId1 = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("사랑", "연인"), false, null));
        postId2 = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("학업", "친"), false, null));
        feedAlgorithmService.runScheduledFeedAlgorithm();
    }

    @Test
    public void getFeedsAtHome() {
        Page<FeedDTO.GetFeedsAtHomeResponse> result = feedService.getFeedsAtHome(userId2, PageRequest.of(0, 30));
        assertThat(result.getContent().get(0).getPostId(), is(postId1));
    }
}