package com.moca.springboot.repository;

import com.moca.springboot.entity.Comment;
import com.moca.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    long countByPost(Post post);
}