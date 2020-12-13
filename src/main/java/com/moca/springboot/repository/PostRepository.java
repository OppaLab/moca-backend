package com.moca.springboot.repository;

import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable pageable);

    long countByUser(User user);

    Page<Post> findByPostCategoriesCategoryName(String category, Pageable pageable);

    Page<Post> findByPostTitleContainingOrPostBodyContaining(String search, String search1, Pageable pageable);

    Page<Post> findByUserNot(User user, Pageable pageable);

    Page<Post> findByPostId(Long postId, Pageable pageable);

    List<Post> findByUser_UserId(long userId);

    @Transactional
    void deleteAllByUser(User user);
}
