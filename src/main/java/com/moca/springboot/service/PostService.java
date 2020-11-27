package com.moca.springboot.service;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.entity.*;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Autowired
    NaturalLanguageApiService naturalLanguageApiService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    PostEntityRepository postEntityRepository;

    @Autowired
    FcmService fcmService;

    //    @Value("${ncp.accesskey}")
//    private String accessKey;
//    @Value("${ncp.secretkey}")
//    private String secretKey;
    @Value("${image.thumbnail.basedir}")
    private String basedir;


    public long createPost(PostDTO.CreatePostRequest createPostRequest) throws IOException, FirebaseMessagingException {

        Post post = new Post();
        post.setPostTitle(createPostRequest.getPostTitle());
        post.setPostBody(createPostRequest.getPostBody());
        post.setThumbnailImageFilePath(createPostRequest.getThumbnailImageFilePathName());
//        post.setCreatedAt(LocalDateTime.now());
        User user = new User(createPostRequest.getUserId());
        post.setUser(user);

        List<PostCategory> postCategory = new ArrayList<>();
        for (String categoryName : createPostRequest.getPostCategories()) {
            postCategory.add(new PostCategory(categoryName, post));
            post.getPostCategories().addAll(postCategory);
        }
        Post newPost = postRepository.save(post);
        postCategoryRepository.saveAll(postCategory);

        List<Follow> follows = followRepository.findByFollowedUser(user);
        for (Follow follow : follows) {
            Activity activity = new Activity();
            activity.setUser(follow.getFollowedUser());
            activity.setToUser(follow.getUser());
            activity.setActivity("post");
            activity.setPost(newPost);
            activityRepository.save(activity);
        }

        // async 로 일단 post id를 돌려주고 감정분석 및 정량화 실행
        naturalLanguageApiService.naturalLanguageApi(createPostRequest, newPost);

        // 고민글 랜덤 푸시
        if (createPostRequest.getIsRandomUserPushNotification())
            fcmService.sendToRandomUser(createPostRequest.getUserId(),
                    newPost,
                    createPostRequest.getNumberOfRandomUserPushNotification());

        return newPost.getPostId();
    }

    public long deletePost(long postId, long userId) {
        Post post = new Post();
        post.setPostId(postId);
        if (postRepository.findById(postId).get().getUser().getUserId() == userId) {
            feedRepository.deleteAllByPost(post);
            List<Comment> comments = commentRepository.findByPost_PostId(postId);
            activityRepository.deleteAllByPost(post);
            comments.forEach(comment -> activityRepository.deleteAllByComment(comment));
            commentRepository.deleteAllByPost(post);
            likeRepository.deleteAllByPost(post);
            postCategoryRepository.deleteAllByPost(post);
            postEntityRepository.deleteAllByPost(post);
            reviewRepository.deleteByPost(post);
            postRepository.delete(post);
        }

        return post.getPostId();
    }

    public String saveThumbnailImageFile(PostDTO.CreatePostRequest createPostRequest) {
//        final String endPoint = "https://kr.object.ncloudstorage.com";
//        final String regionName = "kr-standard";
//
//// S3 client
//        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//
//
//        String bucketName = "thumbnail-images";
//
//// create folder
//        String folderName = createPostRequest.getUserId() + "/";
//
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(0L);
//        objectMetadata.setContentType("application/x-directory");
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);
//
//        try {
//            s3.putObject(putObjectRequest);
//            System.out.format("Folder %s has been created.\n", folderName);
//        } catch (AmazonS3Exception e) {
//            e.printStackTrace();
//        } catch (SdkClientException e) {
//            e.printStackTrace();
//        }
//
//// upload local file
//        UUID uid = UUID.randomUUID();
//        String fileExtension = StringUtils.getFilenameExtension(createPostRequest.getThumbnailImageFile().getOriginalFilename());
//        String objectName = folderName + uid + "." + fileExtension;
//        objectMetadata.setContentType(createPostRequest.getThumbnailImageFile().getContentType());
//        objectMetadata.setContentLength(createPostRequest.getThumbnailImageFile().getSize());
//
////        objectMetadata.setHeader("filename", createPostRequest.getThumbnailImageFile().getOriginalFilename());
//        try {
//            s3.putObject(new PutObjectRequest(bucketName,objectName,createPostRequest.getThumbnailImageFile().getInputStream(),objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
////            s3.putObject(bucketName, objectName, createPostRequest.getThumbnailImageFile().getInputStream(), objectMetadata).;
//            System.out.format("Object %s has been created.\n", objectName);
//        } catch (AmazonS3Exception e) {
//            e.printStackTrace();
//        } catch (SdkClientException | IOException e) {
//            e.printStackTrace();
//        }
//        return endPoint + "/thumbnail-images/" + objectName;
        // TODO: application.properties의 images.basedir을 배포시 맞게 변경
        UUID uid = UUID.randomUUID();
//        String fileExtension = StringUtils.getFilenameExtension(createPostRequest.getThumbnailImageFile().getOriginalFilename());
//        String fileName = uid + "." + fileExtension;
        String fileName = uid + ".png";
        String filePath = basedir + fileName;
        try {
            createPostRequest.getThumbnailImageFile().transferTo(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public Page<PostDTO.GetPostsResponse> getPosts(long userId, Long postId, String search, String category, Pageable pageable) {
        Page<PostDTO.GetPostsResponse> getPostsResponses;
        Page<Post> posts;

        if (postId == null) {
            // 검색어 입력
            if (!search.isEmpty()) {
                // 검색창 default feeds
                if (search.equals("DEFAULT"))
                    posts = postRepository.findByUserNot(new User(userId), pageable);
                else {
                    String sort = null;
                    for (Sort.Order order : pageable.getSort())
                        sort = order.getProperty();
                    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, sort);
                    posts = postRepository.findByPostTitleContainingOrPostBodyContaining(search, search, pageRequest);
                }
            } else {
                // 내 게시글 가져오기
                if (category.isEmpty())
                    posts = postRepository.findByUser(new User(userId), pageable);
                    // 카테고리 입력
                else {
                    String sort = null;
                    for (Sort.Order order : pageable.getSort())
                        sort = order.getProperty();
                    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, sort);
                    posts = postRepository.findByPostCategoriesCategoryName(category, pageRequest);
                }
            }
        } else {
            posts = postRepository.findByPostId(postId, pageable);
        }

        getPostsResponses =
                posts.map(post -> {
                    PostDTO.GetPostsResponse getPostsResponse = new PostDTO.GetPostsResponse();
                    getPostsResponse.setPostId(post.getPostId());
                    getPostsResponse.setPostTitle(post.getPostTitle());
                    getPostsResponse.setPostBody(post.getPostBody());
                    getPostsResponse.setUserId(post.getUser().getUserId());
                    getPostsResponse.setNickname(post.getUser().getNickname());
                    getPostsResponse.setProfileImageFilePath(post.getUser().getProfileImageFilePath());
                    // 만들어진 시각부터 지금까지의 시간(초단위)을 보냄
                    getPostsResponse.setCreatedAt((new Date().getTime() - post.getCreatedAt().getTime()) / 1000);
                    getPostsResponse.setThumbnailImageFilePath(post.getThumbnailImageFilePath());
                    getPostsResponse.setLike(Boolean.FALSE);
                    likeRepository.findByUserAndPost(new User(userId), post).ifPresent(action ->
                            getPostsResponse.setLike(Boolean.TRUE));
                    getPostsResponse.setLikeCount(post.getLikeCount());
                    getPostsResponse.setCommentCount(post.getCommentCount());
                    getPostsResponse.setCategories(post.getPostCategories().stream().
                            map(postCategory -> postCategory.getCategoryName()).collect(Collectors.toList()));
                    if (post.getReview() != null)
                        getPostsResponse.setReviewId(post.getReview().getReviewId());

                    return getPostsResponse;
                });
        return getPostsResponses;
    }

    public Resource getThumbnailImage(String fileName) throws MalformedURLException {
        Path path = Paths.get(basedir + fileName);
        return new UrlResource(path.toUri());
    }

    public long deleteReview(long reviewId, long userId) {
        Review review = new Review();
        review.setReviewId(reviewId);
        if (reviewRepository.findById(reviewId).get().getUser().getUserId() == userId) {
            activityRepository.deleteAllByReview(review);
            likeRepository.deleteAllByReview(review);
            commentRepository.deleteAllByReview(review);
            reviewRepository.delete(review);
        }
        return reviewId;
    }

    public long updatePost(String postId, PostDTO.UpdatePostRequest updatePostRequest) throws IOException {

        Post post = postRepository.findById(Long.parseLong(postId)).get();
        if (updatePostRequest.getThumbnailImageFile() != null) {
            PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
            createPostRequest.setThumbnailImageFile(updatePostRequest.getThumbnailImageFile());
            post.setThumbnailImageFilePath(saveThumbnailImageFile(createPostRequest));
        }

        postCategoryRepository.deleteAllByPost(post);
        List<PostCategory> postCategories = new ArrayList<>();
        for (String categoryName : updatePostRequest.getPostCategories()) {
            postCategories.add(new PostCategory(categoryName, post));
        }
        postCategoryRepository.saveAll(postCategories);

        Post updatedPost;
        if (!updatePostRequest.getPostTitle().equals(post.getPostTitle()) || !updatePostRequest.getPostBody().equals(post.getPostBody())) {
            if (!updatePostRequest.getPostTitle().equals(post.getPostTitle())) {
                post.setPostTitle(updatePostRequest.getPostTitle());
            }
            if (!updatePostRequest.getPostBody().equals(post.getPostBody())) {
                post.setPostBody(updatePostRequest.getPostBody());
            }
            updatedPost = postRepository.save(post);
            postEntityRepository.deleteAllByPost(post);
            PostDTO.CreatePostRequest createPostRequest = new PostDTO.CreatePostRequest();
            createPostRequest.setUserId(updatePostRequest.getUserId());
            createPostRequest.setPostTitle(updatePostRequest.getPostTitle());
            createPostRequest.setPostBody(updatePostRequest.getPostBody());
            naturalLanguageApiService.naturalLanguageApi(createPostRequest, updatedPost);
        } else {
            updatedPost = postRepository.save(post);
        }

        return updatedPost.getPostId();
    }

    public long updateReview(String reviewId, PostDTO.UpdateReviewRequest updateReviewRequest) {
        Review review = reviewRepository.findById(Long.parseLong(reviewId)).get();
        if (!updateReviewRequest.getReview().equals(review))
            review.setReview(updateReviewRequest.getReview());
        reviewRepository.save(review);
        return review.getReviewId();
    }
}

