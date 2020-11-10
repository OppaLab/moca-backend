package com.moca.springboot.service;

import com.moca.springboot.dto.responseDto.FeedDTO;
import com.moca.springboot.entity.Feed;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        // TODO: 1. 팔로우한 계정의 최근 3일간 게시물을 홈화면에서 맨처음 피즈해줄 것 인지, 팔로잉한 계정의 고민글은 검색 화면에서 따로 보여줄 것인지 논의가 필요함
        // TODO: 만약 팔로잉 계정의 고민글을 최상단에 보여주려면 고민글이 추가될 때 마다 feed table 에 score 1인 tuple 을 추가
        Page<Feed> feedOrders = feedRepository.findByUser(new User(userId), pageable);
        Page<FeedDTO> feeds;
        if (feedOrders.getNumberOfElements() == 0) {
            PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "createdAt");
            Page<Post> feedsByRecency = postRepository.findAll(pageRequest);
            feeds =
                    feedsByRecency.map(feed -> {
                        FeedDTO feedDTO = new FeedDTO();
                        feedDTO.setPostId(feed.getPostId());
                        feedDTO.setPostTitle(feed.getPostTitle());
                        feedDTO.setPostBody(feed.getPostBody());
                        feedDTO.setUserId(feed.getUser().getUserId());
                        feedDTO.setNickname(feed.getUser().getNickname());
                        // 만들어진 시각부터 지금까지의 시간(초단위)을 보냄
                        feedDTO.setCreatedAt((new Date().getTime() - feed.getCreatedAt().getTime()) / 1000);
                        feedDTO.setThumbnailImageFilePath(feed.getThumbnailImageFilePath());
                        feedDTO.setLike(Boolean.FALSE);
                        likeRepository.findByUserAndPost(new User(userId), feed).ifPresent(action ->
                                feedDTO.setLike(Boolean.TRUE));
                        feedDTO.setLikeCount(likeRepository.countByPost(feed));
                        feedDTO.setCommentCount(commentRepository.countByPost(feed));

                        return feedDTO;
                    });
            return feeds;
        }
        feeds =
                feedOrders.map(feed -> {
                    FeedDTO feedDTO = new FeedDTO();
                    Post post = postRepository.findById(feed.getPost().getPostId()).get();
                    feedDTO.setPostId(post.getPostId());
                    feedDTO.setPostTitle(post.getPostTitle());
                    feedDTO.setPostBody(post.getPostBody());
                    feedDTO.setUserId(post.getUser().getUserId());
                    feedDTO.setNickname(post.getUser().getNickname());
                    // 만들어진 시각부터 지금까지의 시간(초단위)을 보냄
                    feedDTO.setCreatedAt((new Date().getTime() - post.getCreatedAt().getTime()) / 1000);
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
