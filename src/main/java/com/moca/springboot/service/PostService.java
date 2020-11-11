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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public String saveThumbnailImageFile(MultipartFile thumbnailImageFile) {
        UUID uid = UUID.randomUUID();
        String baseUri = "C:\\Users\\jaewa\\IdeaProjects\\MOCA\\src\\main\\resources\\images";
        String fileExtension = StringUtils.getFilenameExtension(thumbnailImageFile.getOriginalFilename());
        String filePathName = baseUri + File.separator + uid + "." + fileExtension;
        try {
            thumbnailImageFile.transferTo(new File(filePathName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePathName;
    }

    public Page<PostDTO.GetMyPostsResponse> getMyPosts(long userId, Pageable pageable) {
        ;
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
}
