package com.moca.springboot.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table(name ="posts")
public class Post {
    @Id
    @GeneratedValue
    private int post_id;
    private String thumbnail_image;
    private String post_title;
    private String post_contents;
    private LocalDateTime updated_time;
    private int user_id;
    private int post_sentiment_score;

}
