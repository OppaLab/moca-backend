package com.moca.springboot.repository;

import com.moca.springboot.entity.Activity;
import com.moca.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByToUser(User user, Pageable pageable);
}
