package com.moca.springboot.repository;

import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);

    Optional<Like> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
}
