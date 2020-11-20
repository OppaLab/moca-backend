package com.moca.springboot.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@RequiredArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long postId;

    private String thumbnailImageFilePath;
    private String postTitle;
    private String postBody;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private float postSentimentScore;

    @OneToMany(mappedBy = "post")
    private List<PostEntity> postEntities;

    @OneToMany(mappedBy = "post")
    private List<PostCategory> postCategories = new ArrayList<>(0);

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Feed> feeds;

    @OneToOne(mappedBy = "post")
    private Review review;

}
