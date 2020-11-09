package com.moca.springboot.controller;

import com.moca.springboot.dto.AddPost;
import com.moca.springboot.entity.Post;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.service.NaturalLanguageApiService;
import com.moca.springboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private NaturalLanguageApiService naturalLanguageApiService;

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
    public @ResponseBody
    long createPost(AddPost addPost) throws IOException {
        long post_id = postService.addPost(addPost);
        return post_id;
    }

    @DeleteMapping("/post{id}")
    public String deletePost(@PathVariable String id){
        Long postID = Long.parseLong(id);
        postRepository.deleteById(postID);

        return "Delete Success";
    }
}
