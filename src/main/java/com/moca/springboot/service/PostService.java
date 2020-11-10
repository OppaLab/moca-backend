package com.moca.springboot.service;


import com.moca.springboot.dto.requestDto.DeletePost;
import com.moca.springboot.dto.requestDto.PostDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostCategory;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.PostCategoryRepository;
import com.moca.springboot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


    public long addPost(PostDTO postDTO) throws IOException {

        Post post = new Post();
        post.setPostTitle(postDTO.getPostTitle());
        post.setPostBody(postDTO.getPostBody());
        post.setThumbnailImageFilePath(postDTO.getThumbnailImageFilePathName());
//        post.setCreatedAt(LocalDateTime.now());
        User user = new User();
        user.setUserId(postDTO.getUserId());
        post.setUser(user);

        List<PostCategory> postCategory = new ArrayList<>();
        for (String categoryName : postDTO.getPostCategories()) {
            postCategory.add(new PostCategory(categoryName, post));
            post.getPostCategories().addAll(postCategory);
        }
        Post newPost = postRepository.save(post);
        postCategoryRepository.saveAll(postCategory);

        // async 로 일단 post id를 돌려주고 감정분석 및 정량화 실행
        naturalLanguageApiService.naturalLanguageApi(postDTO, newPost);

        return newPost.getPostId();
    }

    private long deletePost(DeletePost deletePost) {
        Post post = new Post();
        post.setPostId(deletePost.getUser_id());
        postRepository.delete(post);
        return post.getPostId();
    }

    public String saveThumbnailImageFile(MultipartFile thumbnailImageFile, PostDTO postDTO) {
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
}
