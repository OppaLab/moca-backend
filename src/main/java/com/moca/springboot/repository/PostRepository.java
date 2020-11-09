package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPostId(Long postId);
}
