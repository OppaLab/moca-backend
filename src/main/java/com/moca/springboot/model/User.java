package com.moca.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String nickname;
    private String email;
    private LocalDateTime recentAccessTime;
    private float userSentimentScore;

    @OneToMany(mappedBy = "user")
    private List<UserCategory> userCategoryList;


}
