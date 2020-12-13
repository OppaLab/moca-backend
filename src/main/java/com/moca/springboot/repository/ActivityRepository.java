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

    @Transactional
    void deleteAllByPost_PostIdAndUser_UserIdAndActivity(long parseLong, long userId, String activity);

    @Transactional
    void deleteAllByReview_ReviewIdAndUser_UserIdAndActivity(long parseLong, long userId, String activity);

    @Transactional
    void deleteAllByUser_UserIdAndActivity(long userId, String activity);

    @Transactional
    void deleteAllByUser_UserIdAndToUser_UserIdAndActivity(long followedUserId, long userId, String activity);
}
