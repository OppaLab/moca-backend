package com.moca.springboot.repository;

import com.moca.springboot.entity.Like;
import com.moca.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPost(Post post);
}
