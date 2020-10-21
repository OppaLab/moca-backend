package com.moca.springboot.repository;

import com.moca.springboot.model.Comment;
import org.springframework.data.repository.CrudRepository;


public interface CommentRepository extends CrudRepository<Comment, Long> {

}

