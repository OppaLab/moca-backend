package com.moca.springboot.service;


import com.moca.springboot.dto.AddPost;
import com.moca.springboot.dto.DeletePost;
import com.moca.springboot.model.Post;
import com.moca.springboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;


    public long addPost(AddPost addPost){
        Post post = new Post();
        post.setPost_id(addPost.getPost_id());
        post.setPost_title(addPost.getPost_title());
        post.setPost_contents(addPost.getPost_contents());
        post.setPost_sentiment_score(addPost.getPost_centiment_score());
        post.setThumbnail_image(addPost.getThumbnail_image());
        post.setUpdated_time(LocalDateTime.now());

        Post newPost = postRepository.save(post);

        return newPost.getPost_id();
    }

    private long deletePost(DeletePost deletePost) {
        Post post = new Post();
        post.setPost_id(deletePost.getUser_id());
        postRepository.delete(post);
        return post.getPost_id();
    }
}
