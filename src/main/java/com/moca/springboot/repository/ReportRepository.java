package com.moca.springboot.repository;

import com.moca.springboot.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostNotNullOrderByCreatedAtDesc();

    List<Report> findByReviewNotNullOrderByCreatedAtDesc();

    List<Report> findByCommentNotNullOrderByCreatedAtDesc();

    List<Report> findByPostIsNullAndReviewIsNullAndCommentIsNullOrderByCreatedAtDesc();
}
