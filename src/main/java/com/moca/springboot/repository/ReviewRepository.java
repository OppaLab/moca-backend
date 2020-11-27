package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.Review;
import com.moca.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Transactional
    void deleteByPost(Post post);

    @Transactional
    void deleteAllByUser(User user);
}
