package com.moca.springboot.repository;

import com.moca.springboot.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByToUser(User user, Pageable pageable);

    @Transactional
    void deleteAllByPost(Post post);

    @Transactional
    void deleteAllByComment(Comment comment);

    @Transactional
    void deleteAllByReview(Review review);

    @Transactional
    void deleteAllByUserOrToUser(User user, User toUser);
}
