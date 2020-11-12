package com.moca.springboot.service;


import com.moca.springboot.dto.PostDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostCategory;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.CommentRepository;
import com.moca.springboot.repository.LikeRepository;
import com.moca.springboot.repository.PostCategoryRepository;
import com.moca.springboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Value("${ncp.accesskey}")
    private String accessKey;
    @Value("${ncp.secretkey}")
    private String secretKey;
    @Value("${image.basedir}")
    private String basedir;


    public long createPost(PostDTO.CreatePostRequest createPostRequest) throws IOException {

        Post post = new Post();
        post.setPostTitle(createPostRequest.getPostTitle());
        post.setPostBody(createPostRequest.getPostBody());
        post.setThumbnailImageFilePath(createPostRequest.getThumbnailImageFilePathName());
//        post.setCreatedAt(LocalDateTime.now());
        User user = new User();
        user.setUserId(createPostRequest.getUserId());
        post.setUser(user);

        List<PostCategory> postCategory = new ArrayList<>();
        for (String categoryName : createPostRequest.getPostCategories()) {
            postCategory.add(new PostCategory(categoryName, post));
            post.getPostCategories().addAll(postCategory);
        }
        Post newPost = postRepository.save(post);
        postCategoryRepository.saveAll(postCategory);

        // async 로 일단 post id를 돌려주고 감정분석 및 정량화 실행
        naturalLanguageApiService.naturalLanguageApi(createPostRequest, newPost);

        return newPost.getPostId();
    }

    public long deletePost(long postId, long userId) {
        Post post = new Post();
        post.setPostId(userId);
        if (postRepository.findById(postId).get().getUser().getUserId() == userId)
            postRepository.delete(post);
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
        String fileExtension = StringUtils.getFilenameExtension(createPostRequest.getThumbnailImageFile().getOriginalFilename());
        String fileName = uid + "." + fileExtension;
        String filePath = basedir + fileName;
        try {
            createPostRequest.getThumbnailImageFile().transferTo(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public Page<PostDTO.GetMyPostsResponse> getMyPosts(long userId, Pageable pageable) {

        Page<PostDTO.GetMyPostsResponse> getMyPostsResponses;
        Page<Post> posts = postRepository.findByUser(new User(userId), pageable);
        getMyPostsResponses =
                posts.map(post -> {
                    PostDTO.GetMyPostsResponse getMyPostsResponse = new PostDTO.GetMyPostsResponse();
                    getMyPostsResponse.setPostId(post.getPostId());
                    getMyPostsResponse.setPostTitle(post.getPostTitle());
                    getMyPostsResponse.setPostBody(post.getPostBody());
                    getMyPostsResponse.setUserId(post.getUser().getUserId());
                    getMyPostsResponse.setNickname(post.getUser().getNickname());
                    // 만들어진 시각부터 지금까지의 시간(초단위)을 보냄
                    getMyPostsResponse.setCreatedAt((new Date().getTime() - post.getCreatedAt().getTime()) / 1000);
                    getMyPostsResponse.setThumbnailImageFilePath(post.getThumbnailImageFilePath());
                    getMyPostsResponse.setLike(Boolean.FALSE);
                    likeRepository.findByUserAndPost(new User(userId), post).ifPresent(action ->
                            getMyPostsResponse.setLike(Boolean.TRUE));
                    getMyPostsResponse.setLikeCount(likeRepository.countByPost(post));
                    getMyPostsResponse.setCommentCount(commentRepository.countByPost(post));

                    return getMyPostsResponse;
                });
        return getMyPostsResponses;
    }

    public Resource getThumbnailImage(String fileName) throws MalformedURLException {
        Path path = Paths.get(basedir + fileName);
        return new UrlResource(path.toUri());
    }
}
