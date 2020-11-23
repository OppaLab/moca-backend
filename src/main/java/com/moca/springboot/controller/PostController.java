package com.moca.springboot.controller;

import com.moca.springboot.dto.FeedDTO;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.service.FeedService;
import com.moca.springboot.service.PostService;
import com.moca.springboot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FeedService feedService;

    @GetMapping("/feed")
    public Page<FeedDTO.GetFeedsAtHomeResponse> getFeedsAtHome(@RequestParam(value = "userId") long userId,
                                                               @PageableDefault(size = 30, sort = "score", direction = Sort.Direction.DESC) Pageable pageable) {
        return feedService.getfeedsAtHome(userId, pageable);
    }

    @GetMapping("/post")
    public Page<PostDTO.GetPostsResponse> getPosts(@RequestParam(value = "userId") long userId,
                                                   @RequestParam(value = "postId", required = false) Long postId,
                                                   @RequestParam(value = "search") String search,
                                                   @RequestParam(value = "category") String category,
                                                   @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return postService.getPosts(userId, postId, search, category, pageable);
    }

    @PostMapping("/post")
    public long createPost(PostDTO.CreatePostRequest createPostRequest) throws IOException {
        createPostRequest.setThumbnailImageFilePathName(postService.saveThumbnailImageFile(createPostRequest));
        return postService.createPost(createPostRequest);
    }

    @DeleteMapping("/post")
    public long deletePost(@RequestParam(value = "postId") long postId, @RequestParam(value = "userId") long userId) {
        postService.deletePost(postId, userId);
        return postId;
    }

    @GetMapping("/review")
    public PostDTO.GetReviewResponse getReview(@RequestParam(value = "userId") long userId, @RequestParam(value = "reviewId") long reviewId) {
        return reviewService.getReview(userId, reviewId);
    }


    @PostMapping("/review")
    public long createReview(PostDTO.CreateReviewRequest createReviewRequest) {
        return reviewService.createReview(createReviewRequest);
    }

    @DeleteMapping("/review")
    public long deleteReview(@RequestParam(value = "reviewId") long reviewId, @RequestParam(value = "userId") long userId) {
        return postService.deleteReview(reviewId, userId);
    }

    @GetMapping("/image/thumbnail/{fileName}")
    public ResponseEntity<Resource> getThumbnailImage(@PathVariable(value = "fileName") String fileName, HttpServletRequest request) throws IOException {
        Resource resource = postService.getThumbnailImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(request.getServletContext().
                        getMimeType(resource.getFile().getAbsolutePath())))
                .body(resource);
    }
}
