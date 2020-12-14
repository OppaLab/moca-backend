package com.moca.springboot.service;

import com.moca.springboot.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private Long userId1;
    private Long userId2;

    @Before
    public void setUp() {
        userId1 = userService.signUp(new UserDTO.SignUpRequest("batman", Arrays.asList("사랑"), "jaewan9074@gmail.com", "1234"));
        userId2 = userService.signUp(new UserDTO.SignUpRequest("ironman", Arrays.asList("사랑"), "jaewan9074@naver.com", "1234"));
    }

    @Test
    public void signUp() {
        Long result = userService.signUp(new UserDTO.SignUpRequest("superman", Arrays.asList("사랑"), "jaewan9074@ajou.ac.kr", "123"));
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void signIn() {
        Long result = userService.signIn(new UserDTO.SignInRequest("jaewan9074@gmail.com"));
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void followUser() {
        Long result = userService.followUser(new UserDTO.FollowRequest(userId1, userId2));
        assertThat(result, is(notNullValue()));
        assertThat(result, is(userId1));
    }

    @Test
    public void unfollowUser() {
        Long result = userService.unfollowUser(userId1, userId2);
        assertThat(result, is(notNullValue()));
        assertThat(result, is(userId1));
    }

    @Test
    public void getProfile() {
        UserDTO.GetProfileResponse result = userService.getProfile(userId1, userId2);
        assertThat(result, is(notNullValue()));
        assertThat(result.getNickname(), is("ironman"));
    }

    @Test
    public void updateProfile() {
        long result = userService.updateProfile(userId1, new UserDTO.UpdateProfileRequest("antman", Arrays.asList("학업"),true));
        assertThat(result, is(userId1));
    }

    @Test
    public void signOut() {
        long result = userService.signOut(userId1);
        assertThat(result, is(userId1));
    }
}