package com.moca.springboot.repository;

import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
