package com.moca.springboot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long userId;
    private String nickname;
    private String email;
    private Date createdAt;
    private float userSentimentScore;
    private String profileImageFilePath;
    private Boolean subscribeToPushNotification;
    private String registrationToken;

    @OneToMany(mappedBy = "user")
    private List<UserCategory> userCategories;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<UserEntity> userEntities;

    @OneToMany(mappedBy = "user")
    private List<Feed> feeds;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToMany(mappedBy = "user")
    private List<Follow> follow;

    @OneToMany(mappedBy = "followedUser")
    private List<Follow> followed;

    @OneToMany(mappedBy = "user")
    private List<Activity> activityBy;

    @OneToMany(mappedBy = "toUser")
    private List<Activity> activityTo;

}
