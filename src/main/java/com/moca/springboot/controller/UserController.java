package com.moca.springboot.controller;

import com.moca.springboot.dto.UserDTO;
import com.moca.springboot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = {"1. User"})
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "회원가입", notes = "처음 구글 로그인시 회원가입을진행하고 인증 쿠키를 발급합니다")
    @PostMapping("/signup")
    public Long signUp(UserDTO.SignUpRequest signUpRequest, HttpServletResponse response) {

        Long userId = userService.signUp(signUpRequest);

        Cookie session = new Cookie("userId", userId.toString());
        session.setMaxAge(60 * 60 * 24 * 30 * 3);

        response.addCookie(session);
        return userId;
    }

    @PostMapping("/signin")
    public Long signIn(UserDTO.SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/image/profile")
    public String setProfileImage(UserDTO.SetProfileImageRequest setProfileImageRequest) {
        return userService.setProfileImage(setProfileImageRequest);
    }

    @PostMapping("/follow")
    public Long followUser(UserDTO.FollowRequest followRequest) {
        return userService.followUser(followRequest);
    }

    @DeleteMapping("/unfollow")
    public Long unfollowUser(@RequestParam(value = "userId") long userId, @RequestParam(value = "followedUserId") long followedUserId) {
        return userService.unfollowUser(userId, followedUserId);
    }

    @GetMapping("/profile")
    public UserDTO.GetProfileResponse getProfile(@RequestParam(value = "myUserId") long myUserId, @RequestParam(value = "userId", required = false) long userId) {
        return userService.getProfile(myUserId, userId);
    }

    @GetMapping("/image/profile/{fileName}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable(value = "fileName") String fileName, HttpServletRequest request) throws IOException {
        Resource resource = userService.getProfileImage(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(request.getServletContext().
                        getMimeType(resource.getFile().getAbsolutePath())))
                .body(resource);
    }

    @PostMapping("/subscribe")
    public String subscribeToPushNotification(UserDTO.UpdateSubscribeRequest updateSubscribeRequest) {
        return userService.subscribeToPushNotification(updateSubscribeRequest.getUserId());
    }

    @PostMapping("/unsubscribe")
    public String unsubscribeToPushNotification(UserDTO.UpdateSubscribeRequest updateSubscribeRequest) {
        return userService.unsubscribeToPushNotification(updateSubscribeRequest.getUserId());
    }
}
