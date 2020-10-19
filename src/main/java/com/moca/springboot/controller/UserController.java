package com.moca.springboot.controller;

import com.moca.springboot.dto.SignUp;
import com.moca.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Long signUp(SignUp signUp, HttpServletResponse response) {


        Long userId = userService.signUp(signUp);

        Cookie session = new Cookie("userId", userId.toString());
        session.setMaxAge(60 * 60 * 24 * 30 * 3);

        response.addCookie(session);
        return userId;

    }
}
