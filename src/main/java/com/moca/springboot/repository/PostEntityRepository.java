package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostEntity;
import com.moca.springboot.entity.PostEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostEntityRepository extends JpaRepository<PostEntity, PostEntityPK> {
    List<PostEntity> findByPost(Post post);
//    Optional<Post> findByEntityAndPostId(String entity, Long postId);

}
