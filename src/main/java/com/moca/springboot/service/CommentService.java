package com.moca.springboot.service;

import com.moca.springboot.dto.CreateComment;
import com.moca.springboot.dto.DeleteComment;
import com.moca.springboot.model.Comment;
import com.moca.springboot.repository.CommentRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public long createComment(CreateComment createComment) {

        Comment comment = new Comment();
        comment.setComment_id(createComment.getComment_id());
        comment.setPost_id(createComment.getPost_id());
        comment.setReview_id(createComment.getReview_id());
        comment.setUser_id(createComment.getUser_id());
        comment.setText(createComment.getComment_text());
//        comment.setCreatedTime(LocalDateTime.now());

        Comment newComment = commentRepository.save(comment);

        return newComment.getComment_id();
    }

    public long deleteComment(DeleteComment deleteComment) {
        Comment comment = new Comment();
        comment.setComment_id(deleteComment.getComment_id());
        commentRepository.delete(comment);
        return comment.getComment_id();
    }
}
