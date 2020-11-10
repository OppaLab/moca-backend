package com.moca.springboot.controller;

import com.moca.springboot.dto.requestDto.PostDTO;
import com.moca.springboot.dto.requestDto.ReviewDTO;
import com.moca.springboot.dto.responseDto.FeedDTO;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.service.FeedService;
import com.moca.springboot.service.PostService;
import com.moca.springboot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FeedService feedService;

/*    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable String id) {
        Long postID = Long.parseLong(id);

        Optional<Post> post = postRepository.findById(postID);

        return post.get();
    }*/

    /*    @GetMapping("/post")
        public List<Post> getAllPost() {
            return postRepository.findAll();
        }*/
    @GetMapping("/post")
    public Page<FeedDTO> feedPost(@RequestParam(value = "userId") long userId,
                                  @PageableDefault(size = 10, sort = "score", direction = Sort.Direction.DESC) Pageable pageable) {
        return feedService.feed(userId, pageable);
    }

    @PostMapping("/post")
    public long addPost(@RequestPart("thumbnailImageFile") MultipartFile thumbnailImageFile, PostDTO postDTO) throws IOException {
        postDTO.setThumbnailImageFilePathName(postService.saveThumbnailImageFile(thumbnailImageFile, postDTO));
        return postService.addPost(postDTO);
    }

/*    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable String id) {
        Long postID = Long.parseLong(id);
        postRepository.deleteById(postID);

        return "Delete Success";
    }*/

    @PostMapping("/review")
    public long addReview(ReviewDTO reviewDTO) {
        return reviewService.addReview(reviewDTO);
    }
}
