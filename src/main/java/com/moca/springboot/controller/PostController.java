package com.moca.springboot.controller;

import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.dto.ReviewDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.service.PostService;
import com.moca.springboot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable String id) {
        Long postID = Long.parseLong(id);

        Optional<Post> post = postRepository.findById(postID);

        return post.get();
    }

    @GetMapping("/post")
    public List<Post> getAllPost() {
        return postRepository.findAll();

    }

    @PostMapping("/post")
    public long addPost(@RequestPart("thumbnailImageFile") MultipartFile thumbnailImageFile, PostDTO postDTO) throws IOException {
        postDTO.setThumbnailImageFilePathName(postService.saveThumbnailImageFile(thumbnailImageFile, postDTO));
        return postService.addPost(postDTO);
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable String id) {
        Long postID = Long.parseLong(id);
        postRepository.deleteById(postID);

        return "Delete Success";
    }

    @PostMapping("/review")
    public long addReview(ReviewDTO reviewDTO) {
        return reviewService.addReview(reviewDTO);
    }
}
