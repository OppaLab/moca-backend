package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostEntity;
import com.moca.springboot.entity.pk.PostEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PostEntityRepository extends JpaRepository<PostEntity, PostEntityPK> {
    List<PostEntity> findByPost(Post post);

    @Transactional
    void deleteAllByPost(Post post);
}
