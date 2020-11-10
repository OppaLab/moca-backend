package com.moca.springboot.service;

import com.moca.springboot.dto.CommentDTO;
import com.moca.springboot.dto.DeleteComment;
import com.moca.springboot.entity.Comment;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public long createComment(CommentDTO commentDTO) {

        Comment comment = new Comment();
        Post post = new Post();
        post.setPostId(commentDTO.getPostId());
        comment.setPost(post);
//        comment.setReviewId(commentDTO.getReviewId());
        comment.setUser(new User(commentDTO.getUserId()));
        comment.setComment(commentDTO.getComment());

        Comment newComment = commentRepository.save(comment);

        return newComment.getCommentId();
    }

    public long deleteComment(DeleteComment deleteComment) {

        Comment comment = new Comment();
        comment.setCommentId(deleteComment.getComment_id());
        commentRepository.delete(comment);
        return comment.getCommentId();
    }
}
