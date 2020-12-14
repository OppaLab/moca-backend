package com.moca.springboot.service;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.entity.Comment;
import com.moca.springboot.repository.CommentRepository;
import org.junit.After;
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
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

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
    public void createComment() {
        Long result = commentService.createComment(new CommentDTO.CreateCommentRequest(postId.toString(), "", userId, "공감갑니다."));
        assertNotNull(result);
    }

    @Test
    public void deleteComment() {
        Long commentId = commentService.createComment(new CommentDTO.CreateCommentRequest(postId.toString(), "", userId, "공감갑니다."));
        Long result = commentService.deleteComment(commentId);
        assertNotNull(result);
        assertThat(result, is(commentId));
    }

    @Test
    public void getComments() {
        commentService.createComment(new CommentDTO.CreateCommentRequest(postId.toString(), "", userId, "공감갑니다."));
        Page<CommentDTO.GetCommentsResponse> result = commentService.getComments(postId.toString(), "", PageRequest.of(0, 30));
        assertNotNull(result);
        assertThat(result.getContent().get(0).getComment(), is("공감갑니다."));
    }

    @Test
    public void deleteCommentByAdmin() {
        Long commentId = commentService.createComment(new CommentDTO.CreateCommentRequest(postId.toString(), "", userId, "공감갑니다."));
        commentService.deleteCommentByAdmin(commentId);
        Optional<Comment> deletedComment = commentRepository.findById(commentId);
        assertThat(deletedComment, is(Optional.empty()));

    }
}