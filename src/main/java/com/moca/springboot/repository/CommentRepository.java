package com.moca.springboot.repository;

import com.moca.springboot.entity.Comment;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByPost(Post post);

    Page<Comment> findByPost(Post post, Pageable pageable);

    Page<Comment> findByReview(Review review, Pageable pageable);

    long countByReview(Review review);

    @Transactional
    void deleteAllByPost(Post post);

    List<Comment> findByPost_PostId(long postId);

    @Transactional
    void deleteAllByComment(Comment comment);

    @Transactional
    void deleteAllByReview(Review review);

    @Transactional
    void deleteAllByUser(User user);

    List<Comment> findByPost_PostIdOrReview_ReviewId(long postId, Long reviewId);
}