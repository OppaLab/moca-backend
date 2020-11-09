package com.moca.springboot.repository;

import com.moca.springboot.entity.Feed;
import com.moca.springboot.entity.FeedPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, FeedPK> {
}
