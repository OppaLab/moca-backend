package com.moca.springboot.repository;

import com.moca.springboot.entity.Follow;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.pk.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
    long countByFollowedUser(User followedUser);

    long countByUser(User user);
}
