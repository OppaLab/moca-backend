package com.moca.springboot.repository;

import com.moca.springboot.entity.Feed;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.pk.FeedPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface FeedRepository extends JpaRepository<Feed, FeedPK> {
    Page<Feed> findByUser(User user, Pageable pageable);

    @Transactional
    void deleteAllByPost(Post post);

    @Transactional
    void deleteAllByUser(User user);
}
