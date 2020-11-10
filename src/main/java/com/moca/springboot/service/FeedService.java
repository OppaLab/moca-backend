//package com.moca.springboot.service;
//
//import com.moca.springboot.entity.*;
//import com.moca.springboot.model.Score;
//import com.moca.springboot.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//@Component
//public class FeedService {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    UserCategoryRepository userCategoryRepository;
//    @Autowired
//    PostCategoryRepository postCategoryRepository;
//    @Autowired
//    PostEntityRepository postEntityRepository;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    LikeRepository likeRepository;
//    @Autowired
//    CommentRepository commentRepository;
//    @Autowired
//    UserEntityRepository userEntityRepository;
//    @Autowired
//    FeedRepository feedRepository;
//
//    // 매일 30분마다 알고리즘 실행
//    @Scheduled(cron = "0 0/30 * * * *")
//    public void runFeedAlgorithm() {
//
//        List<Post> posts = postRepository.findAll();
//        List<User> users = userRepository.findAll();
//        List<Score> scores = new ArrayList<>();
//        Score score;
//
//        // user의 category와 post의 category비교
//        List<PostCategory> postCategories;
//        List<UserCategory> userCategories;
//        for (User user : users) {
//            userCategories = userCategoryRepository.findByUser(user);
//
//            List<String> userCategoryNames = new ArrayList<>();
//            for (UserCategory userCategory : userCategories) {
//                userCategoryNames.add(userCategory.getCategoryName());
//            }
//
//            for (Post post : posts) {
//                score = new Score();
//                score.setPostId(post.getPostId());
//                score.setUserId(user.getUserId());
//
//                postCategories = postCategoryRepository.findByPost(post);
//
//                List<String> postCategoryNames = new ArrayList<>();
//                for (PostCategory postCategory : postCategories) {
//                    postCategoryNames.add(postCategory.getCategoryName());
//                }
//
//                for (String userCategoryName : userCategoryNames) {
//                    if (postCategoryNames.contains(userCategoryName)) {
//                        score.setCategoryScore(score.getCategoryScore() + 1.0f);
//                    }
//                }
//                scores.add(score);
//            }
//        }
//
//        // user의 entity와 post의 entity비교
//        List<UserEntity> userEntities;
//        List<PostEntity> postEntities;
//
//        for (User user : users) {
//            userEntities = userEntityRepository.findByUser(user);
//
//            List<String> userEntityNames = new ArrayList<>();
//            for (UserEntity userEntity : userEntities) {
//                userEntityNames.add(userEntity.getEntity());
//            }
//
//            for (Post post : posts) {
//                score = scores.stream()
//                        .filter(object -> object.getUserId() == user.getUserId()
//                                && object.getPostId() == post.getPostId()).findFirst().get();
//
//
//                postEntities = postEntityRepository.findByPost(post);
//
//                List<String> postEntityNames = new ArrayList<>();
//                for (PostEntity postEntity : postEntities) {
//                    postEntityNames.add(postEntity.getEntity());
//                }
//
//                for (String userEntityName : userEntityNames) {
//                    if (postEntityNames.contains(userEntityName)) {
//                        score.setEntityScore(score.getEntityScore() + 1.0f);
//                    }
//                }
//            }
//        }
//
//        //post의 recency 비교
//        posts.sort(Comparator.comparing(Post::getCreatedAt));
//
//        for (User user : users) {
//            int recencyScore = 0;
//            for (Post post : posts) {
//                score = scores.stream()
//                        .filter(object -> object.getUserId() == user.getUserId()
//                                && object.getPostId() == post.getPostId()).findFirst().get();
//                score.setRecencyScore(recencyScore++);
//            }
//        }
//
//
//        // post의 popularity 비교
//
//        List<Like> likes;
//        List<Comment> comments;
//
//        for (User user : users) {
//            for (Post post : posts) {
//                score = scores.stream()
//                        .filter(object -> object.getUserId() == user.getUserId()
//                                && object.getPostId() == post.getPostId()).findFirst().get();
//
//                likes = likeRepository.findByPost(post);
//                comments = commentRepository.findByPost(post);
//
//                score.setPopularityScore(likes.size() + comments.size());
//            }
//        }
//
//        // post와 user의 sentiment score 비교
//        for (User user : users) {
//            for (Post post : posts) {
//                score = scores.stream()
//                        .filter(object -> object.getUserId() == user.getUserId()
//                                && object.getPostId() == post.getPostId()).findFirst().get();
//
//                score.setSentimentScore(Math.abs(user.getUserSentimentScore() + post.getPostSentimentScore()));
//            }
//        }
//
//        // minmax 정규화
//        for (User user : users) {
//            float minCategoryScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .min(Comparator.comparing(Score::getCategoryScore)).get().getCategoryScore();
//            float maxCategoryScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .max(Comparator.comparing(Score::getCategoryScore)).get().getCategoryScore();
//
//            float minEntityScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .min(Comparator.comparing(Score::getEntityScore)).get().getEntityScore();
//            float maxEntityScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .max(Comparator.comparing(Score::getEntityScore)).get().getEntityScore();
//
//            float minRecencyScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .min(Comparator.comparing(Score::getRecencyScore)).get().getRecencyScore();
//            float maxRecencyScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .max(Comparator.comparing(Score::getRecencyScore)).get().getRecencyScore();
//
//            float minPopularityScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .min(Comparator.comparing(Score::getPopularityScore)).get().getPopularityScore();
//            float maxPopularityScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .max(Comparator.comparing(Score::getPopularityScore)).get().getPopularityScore();
//
//            float minSentimentScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .min(Comparator.comparing(Score::getSentimentScore)).get().getSentimentScore();
//            float maxSentimentScore = scores.stream().filter(s -> s.getUserId() == user.getUserId())
//                    .max(Comparator.comparing(Score::getSentimentScore)).get().getSentimentScore();
//
//            for (Post post : posts) {
//                score = scores.stream()
//                        .filter(object -> object.getUserId() == user.getUserId()
//                                && object.getPostId() == post.getPostId()).findFirst().get();
//
//                if (maxCategoryScore == minCategoryScore) {
//                    score.setCategoryScore(0.0f);
//                } else {
//                    score.setCategoryScore((score.getCategoryScore() - minCategoryScore) / (maxCategoryScore - minCategoryScore));
//                }
//                if (maxEntityScore == minEntityScore) {
//                    score.setEntityScore(0.0f);
//                } else {
//                    score.setEntityScore((score.getEntityScore() - minEntityScore) / (maxEntityScore - minEntityScore));
//                }
//                if (maxRecencyScore == minRecencyScore) {
//                    score.setRecencyScore(0.0f);
//                } else {
//                    score.setRecencyScore((score.getRecencyScore() - minRecencyScore) / (maxRecencyScore - minRecencyScore));
//                }
//                if (maxPopularityScore == minPopularityScore) {
//                    score.setPopularityScore(0.0f);
//                } else {
//                    score.setPopularityScore((score.getPopularityScore() - minPopularityScore) / (maxPopularityScore - minPopularityScore));
//                }
//                if (maxSentimentScore == minSentimentScore) {
//                    score.setSentimentScore(0.0f);
//                } else {
//                    score.setSentimentScore((score.getSentimentScore() - minSentimentScore) / (maxSentimentScore - minSentimentScore));
//                }
//
//
//            }
//        }
//
//        // 가중치 곱해서 최종 score 계산
//        for (Score scoreElement : scores) {
//            User user = userRepository.findById(scoreElement.getUserId()).get();
//            Post post = postRepository.findById(scoreElement.getPostId()).get();
//            Feed feed = new Feed();
////            User user = new User();
////            user.setUserId(scoreElement.getUserId());
////            Post post = new Post();
////            post.setPostId(scoreElement.getPostId());
//            feed.setUser(user);
//            feed.setPost(post);
//
//            feed.setScore(
//                    scoreElement.getCategoryScore() * 0.2f +
//                            scoreElement.getEntityScore() * 0.2f +
//                            scoreElement.getRecencyScore() * 0.2f +
//                            scoreElement.getPopularityScore() * 0.2f +
//                            scoreElement.getSentimentScore() * 0.2f);
//
//            feedRepository.findAll();
//            feedRepository.save(feed);
//
//        }
//
//
//    }
//}
