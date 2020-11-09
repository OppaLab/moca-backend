package com.moca.springboot.service;

import com.moca.springboot.dto.SignUpDTO;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserCategory;
import com.moca.springboot.repository.UserCategoryRepository;
import com.moca.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCategoryRepository userCategoryRepository;

    public Long signUp(SignUpDTO signUpDTO) {

        User user = new User();
        user.setNickname(signUpDTO.getNickname());
        user.setEmail(signUpDTO.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUserSentimentScore(0);
        Optional<User> result = userRepository.findByEmail(user.getEmail());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });

        User newUser = userRepository.save(user);
        UserCategory userCategory = new UserCategory();
        signUpDTO.getUserCategoryList().forEach(category -> {
            userCategory.setCategoryName(category);
            userCategory.setUser(newUser);
            userCategoryRepository.save(userCategory);
        });


        return newUser.getUserId();
    }
}
