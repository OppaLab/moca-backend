package com.moca.springboot.repository;

import com.moca.springboot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

}
