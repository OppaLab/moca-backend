package com.moca.springboot.repository;

import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    long countByPost(Post post);

    Optional<Like> findByUserAndReview(User user, Review review);

    long countByReview(Review review);

    List<Like> findByPost(Post post);

    @Transactional
    void deleteAllByPost(Post post);

    @Transactional
    void deleteAllByReview(Review review);

    @Transactional
    void deleteAllByUser(User user);
}
