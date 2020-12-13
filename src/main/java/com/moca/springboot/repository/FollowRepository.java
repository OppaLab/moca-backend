package com.moca.springboot.repository;

import com.moca.springboot.entity.Follow;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.pk.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
    long countByFollowedUser(User followedUser);

    long countByUser(User user);

    List<Follow> findByFollowedUser(User user);

    Optional<Follow> findByUserAndFollowedUser(User user, User followedUser);

    @Transactional
    void deleteAllByUserOrFollowedUser(User user, User followedUser);
}
