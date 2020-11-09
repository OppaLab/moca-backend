package com.moca.springboot.service;


import com.moca.springboot.dto.AddPost;
import com.moca.springboot.dto.DeletePost;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostCategory;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.PostCategoryRepository;
import com.moca.springboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Autowired
    NaturalLanguageApiService naturalLanguageApiService;


    public long addPost(AddPost addPost) throws IOException {

        Post post = new Post();
        post.setPostTitle(addPost.getPostTitle());
        post.setPostBody(addPost.getPostBody());
//        post.setThumbnail_image(post_sentiment_score);
        post.setCreatedAt(LocalDateTime.now());
        User user = new User();
        user.setUserId(addPost.getUserId());
        post.setUser(user);


        List<PostCategory> postCategory = new ArrayList<>();
        for (String categoryName : addPost.getPostCategories()) {
            postCategory.add(new PostCategory(categoryName, post));
            post.getPostCategories().addAll(postCategory);
        }
        Post newPost = postRepository.save(post);
        postCategoryRepository.saveAll(postCategory);

        naturalLanguageApiService.naturalLanguageApi(addPost, newPost);

        return newPost.getPostId();
    }

    private long deletePost(DeletePost deletePost) {
        Post post = new Post();
        post.setPostId(deletePost.getUser_id());
        postRepository.delete(post);
        return post.getPostId();
    }
}
