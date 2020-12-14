package com.moca.springboot.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.ReviewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReviewRepository reviewRepository;

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
    public void createPost() throws IOException, FirebaseMessagingException {
        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        Long result = postService.createPost(new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId, Arrays.asList("학업", "연인"), false, null));
        assertNotNull(result);
    }

    @Test
    public void deletePost() {
        Long result = postService.deletePost(postId);
        assertNotNull(result);
    }

    @Test
    public void saveThumbnailImageFile() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.png", new FileInputStream(new File("/Users/jaewan/IdeaProjects/MOCA-SPRING-BOOT/src/main/resources/images/thumbnails/test.png")));
        String result = postService.saveThumbnailImageFile((new PostDTO.CreatePostRequest(multipartFile,null,"안녕하세요", "반갑습니다.", userId, Arrays.asList("학업", "연인"), false, null)));
        assertNotNull(result);
    }

    @Test
    public void getPosts() {
        Page<PostDTO.GetPostsResponse> result = postService.getPosts(userId, postId, null, null, PageRequest.of(0, 30));
        assertNotNull(result.getContent());
    }

    @Test
    public void getThumbnailImage() throws MalformedURLException {
        Resource result = postService.getThumbnailImage("test.png");
        assertNotNull(result);
    }

    @Test
    public void deleteReview() {
        Long result = postService.deleteReview(reviewId);
        assertNotNull(result);
        assertThat(result, is(reviewId));
    }

    @Test
    public void updatePost() throws IOException {
        Long result = postService.updatePost(postId.toString(), new PostDTO.UpdatePostRequest("안녕하세요 테스트에요", "반갑습니다.", userId, Arrays.asList("사랑")));
        assertNotNull(result);
        assertThat(result, is(postId));
    }

    @Test
    public void updateReview() {
        Long result = postService.updateReview(reviewId.toString(), new PostDTO.UpdateReviewRequest("고생했어"));
        assertNotNull(result);
        assertThat(result, is(reviewId));
    }

    @Test
    public void deletePostByAdmin() {
        postService.deletePostByAdmin(postId);
        Optional<Post> deletedPost = postRepository.findById(postId);
        assertThat(deletedPost, is(Optional.empty()));
    }

    @Test
    public void deleteReviewByAdmin() {
        postService.deleteReviewByAdmin(reviewId);
        Optional<Review> deletedReview = reviewRepository.findById(reviewId);
        assertThat(deletedReview, is(Optional.empty()));
    }
}