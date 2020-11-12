package com.moca.springboot.service;

import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserCategory;
import com.moca.springboot.repository.UserCategoryRepository;
import com.moca.springboot.repository.UserRepository;
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
    private FeedAlgorithmService feedAlgorithmService;
    @Value("${image.profile.basedir}")
    private String basedir;

    public Long signUp(UserDTO.SignUpRequest signUpRequest) {

        User user = new User();
        user.setNickname(signUpRequest.getNickname());
        user.setEmail(signUpRequest.getEmail());
        user.setCreatedAt(new Date());
        user.setUserSentimentScore(0);
        Optional<User> result = userRepository.findByEmail(user.getEmail());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });

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
}
