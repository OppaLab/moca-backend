package com.moca.springboot.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.moca.springboot.dto.*;
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
public class ActivityServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ActivityService activityService;

    private Long userId1;
    private Long userId2;
    private Long postId;

    @Before
    public void setUp() throws Exception {
        userId1 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@gmail.com", "1234"));
        userId2 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@ajou.ac.kr", "1234"));

        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        postId = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId1, Arrays.asList("사랑", "연인"), false, null));
    }

    @Test
    public void getActivities() throws FirebaseMessagingException {
        commentService.createComment(new CommentDTO.CreateCommentRequest(postId.toString(), "", userId2, "좋아요"));
        likeService.createLike(new LikeDTO.CreateLikeRequest(postId.toString(), "", userId2));
        Page<ActivityDTO.GetActivitiesResponse> result = activityService.getActivities(userId1, PageRequest.of(0, 10));
        assertNotNull(result);
        assertThat(result.getContent().get(0).getActivity(), is("comment"));
        assertThat(result.getContent().get(1).getActivity(), is("like"));
    }
}