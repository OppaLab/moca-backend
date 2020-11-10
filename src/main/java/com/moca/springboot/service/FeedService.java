package com.moca.springboot.service;

import com.moca.springboot.dto.responseDto.FeedDTO;
import com.moca.springboot.entity.Feed;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FeedService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    public Page<FeedDTO> feed(long userId, Pageable pageable) {

        Page<Feed> feedOrders = feedRepository.findByUser(new User(userId), pageable);
        Page<FeedDTO> feeds =
                feedOrders.map(feed -> {
                    FeedDTO feedDTO = new FeedDTO();
                    Post post = postRepository.findById(feed.getPost().getPostId()).get();
                    feedDTO.setPostId(post.getPostId());
                    feedDTO.setPostTitle(post.getPostTitle());
                    feedDTO.setPostBody(post.getPostBody());
                    feedDTO.setUserId(post.getUser().getUserId());
                    feedDTO.setNickname(post.getUser().getNickname());
                    // 만들어진 시각부터 지금까지의 시간을 보냄
                    feedDTO.setCreatedAt(new Date().compareTo(post.getCreatedAt()));
                    feedDTO.setThumbnailImageFilePath(post.getThumbnailImageFilePath());
                    feedDTO.setLike(Boolean.FALSE);
                    likeRepository.findByUserAndPost(new User(userId), post).ifPresent(action ->
                            feedDTO.setLike(Boolean.TRUE));
                    feedDTO.setLikeCount(likeRepository.countByPost(post));
                    feedDTO.setCommentCount(commentRepository.countByPost(post));

                    return feedDTO;
                });
        return feeds;
    }
}
