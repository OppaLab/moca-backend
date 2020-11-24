package com.moca.springboot.service;

import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.entity.Activity;
import com.moca.springboot.entity.Follow;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserCategory;
import com.moca.springboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCategoryRepository userCategoryRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private FeedAlgorithmService feedAlgorithmService;


    @Value("${image.profile.basedir}")
    private String basedir;

    public Long signUp(UserDTO.SignUpRequest signUpRequest) {

        User user = new User();
        user.setNickname(signUpRequest.getNickname());
        user.setEmail(signUpRequest.getEmail());
        user.setCreatedAt(new Date());
        user.setUserSentimentScore(0);
//        Optional<User> result = userRepository.findByEmail(user.getEmail());
//        result.ifPresent(m -> {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        });

        User newUser = userRepository.save(user);
        UserCategory userCategory = new UserCategory();
        signUpRequest.getUserCategoryList().forEach(category -> {
            userCategory.setCategoryName(category);
            userCategory.setUser(newUser);
            userCategoryRepository.save(userCategory);
        });

        // 새로 가입한 회원에 대해서 피드 알고리즘 돌림
        feedAlgorithmService.runFeedAlgorithmForNewUser(newUser);

        return newUser.getUserId();
    }

    public String setProfileImage(UserDTO.SetProfileImageRequest setProfileImageRequest) {
        // TODO: application.properties의 images.basedir을 배포시 맞게 변경
        String fileExtension = StringUtils.getFilenameExtension(setProfileImageRequest.getProfileImageFile().getOriginalFilename());
        String fileName = setProfileImageRequest.getUserId() + "." + fileExtension;
        String filePath = basedir + fileName;
        File file = new File(filePath);

        try {
            if (file.exists()) {
                file.delete();
            }
            setProfileImageRequest.getProfileImageFile().transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = userRepository.findById(setProfileImageRequest.getUserId()).get();
        user.setProfileImageFilePath(fileName);
        userRepository.save(user);

        return fileName;
    }


    public Resource getProfileImage(String fileName) throws MalformedURLException {
        Path path = Paths.get(basedir + fileName);
        return new UrlResource(path.toUri());
    }

    public Long signIn(UserDTO.SignInRequest signInRequest) {
        Optional<User> user = userRepository.findByEmail(signInRequest.getEmail());
        if (user.isPresent())
            return userRepository.findByEmail(signInRequest.getEmail()).get().getUserId();
        else
            return null;
    }

    public Long followUser(UserDTO.FollowRequest followRequest) {
        followRepository.save(new Follow(new User(followRequest.getUserId()), new User(followRequest.getFollowedUserId())));

        Activity activity = new Activity();
        activity.setUser(new User(followRequest.getUserId()));
        activity.setToUser(new User(followRequest.getFollowedUserId()));
        activity.setActivity("follow");
        activityRepository.save(activity);

        return followRequest.getUserId();
    }

    public Long unfollowUser(long userId, long followedUserId) {
        followRepository.delete(new Follow(new User(userId), new User(followedUserId)));
        return userId;
    }

    public UserDTO.GetProfileResponse getProfile(long userId) {
        UserDTO.GetProfileResponse getProfileResponse = new UserDTO.GetProfileResponse();
        User user = userRepository.findById(userId).get();
        getProfileResponse.setNickname(user.getNickname());
        getProfileResponse.setProfileImageFilePath(user.getProfileImageFilePath());
        getProfileResponse.setNumberOfPosts(postRepository.countByUser(user));
        getProfileResponse.setNumberOfFollowers(followRepository.countByFollowedUser(user));
        getProfileResponse.setNumberOfFollowings(followRepository.countByUser(user));

        return getProfileResponse;
    }
}
