package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostCategory;
import com.moca.springboot.entity.pk.PostCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryPK> {
    List<PostCategory> findByPost(Post post);

    @Transactional
    void deleteAllByPost(Post post);
}

