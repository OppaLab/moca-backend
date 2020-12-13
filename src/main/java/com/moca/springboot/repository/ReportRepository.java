package com.moca.springboot.repository;

import com.moca.springboot.entity.Report;
import com.moca.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostNotNullOrderByCreatedAtDesc();

    List<Report> findByReviewNotNullOrderByCreatedAtDesc();

    List<Report> findByCommentNotNullOrderByCreatedAtDesc();

    List<Report> findByPostIsNullAndReviewIsNullAndCommentIsNullOrderByCreatedAtDesc();

    Page<Report> findAllByIsHandled(boolean isHandled, Pageable pageable);

    List<Report> findAllByPost_PostId(long postId);

    List<Report> findAllByReview_ReviewId(long reviewId);

    List<Report> findAllByComment_CommentId(long commentId);

    List<Report> findAllByCommentNotNullAndIsHandled(boolean isHandled);

    @Transactional
    void deleteByUser_UserId(long userId);
    @Transactional
    void deleteByReportedUser_UserId(long userId);
}
