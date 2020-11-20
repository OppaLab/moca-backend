package com.moca.springboot.service;

import com.moca.springboot.dto.FeedDTO;
import com.moca.springboot.entity.Feed;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

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

    public Page<FeedDTO.GetFeedsAtHomeResponse> feed(long userId, Pageable pageable) {

        // TODO: 1. 팔로우한 계정의 최근 3일간 게시물을 홈화면에서 맨처음 피즈해줄 것 인지, 팔로잉한 계정의 고민글은 검색 화면에서 따로 보여줄 것인지 논의가 필요함
        // TODO: 만약 팔로잉 계정의 고민글을 최상단에 보여주려면 고민글이 추가될 때 마다 feed table 에 score 1인 tuple 을 추가
        Page<Feed> feedOrders = feedRepository.findByUser(new User(userId), pageable);
        Page<FeedDTO.GetFeedsAtHomeResponse> feedsAtHomeResponses;

        feedsAtHomeResponses =
                feedOrders.map(feedOrder -> {
                    FeedDTO.GetFeedsAtHomeResponse getFeedsAtHomeResponse = new FeedDTO.GetFeedsAtHomeResponse();
                    Post post = postRepository.findById(feedOrder.getPost().getPostId()).get();
                    getFeedsAtHomeResponse.setPostId(post.getPostId());
                    getFeedsAtHomeResponse.setPostTitle(post.getPostTitle());
                    getFeedsAtHomeResponse.setPostBody(post.getPostBody());
                    getFeedsAtHomeResponse.setUserId(post.getUser().getUserId());
                    getFeedsAtHomeResponse.setNickname(post.getUser().getNickname());
                    getFeedsAtHomeResponse.setProfileImageFilePath(post.getUser().getProfileImageFilePath());
                    // 만들어진 시각부터 지금까지의 시간(초단위)을 보냄
                    getFeedsAtHomeResponse.setCreatedAt((new Date().getTime() - post.getCreatedAt().getTime()) / 1000);
                    getFeedsAtHomeResponse.setThumbnailImageFilePath(post.getThumbnailImageFilePath());
                    getFeedsAtHomeResponse.setLike(Boolean.FALSE);
                    likeRepository.findByUserAndPost(new User(userId), post).ifPresent(action ->
                            getFeedsAtHomeResponse.setLike(Boolean.TRUE));
                    getFeedsAtHomeResponse.setLikeCount(likeRepository.countByPost(post));
                    getFeedsAtHomeResponse.setCommentCount(commentRepository.countByPost(post));
                    getFeedsAtHomeResponse.setCategories(post.getPostCategories().stream().
                            map(postCategory -> postCategory.getCategoryName()).collect(Collectors.toList()));
                    if (post.getReview() != null)
                        getFeedsAtHomeResponse.setReviewId(post.getReview().getReviewId());
                    return getFeedsAtHomeResponse;
                });
        return feedsAtHomeResponses;
    }
}
