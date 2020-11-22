package com.moca.springboot.repository;

import com.moca.springboot.entity.Comment;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByPost(Post post);

    Page<Comment> findByPost(Post post, Pageable pageable);

    Page<Comment> findByReview(Review review, Pageable pageable);

    long countByReview(Review review);
}