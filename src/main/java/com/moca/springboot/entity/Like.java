package com.moca.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`like`")
public class Like {
    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    private long reviewId;
    private long userId;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
}
