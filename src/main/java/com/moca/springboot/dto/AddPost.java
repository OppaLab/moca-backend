package com.moca.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddPost {
    int post_id;
    String thumbnail_image;
    String post_title;
    String post_contents;
    LocalDate updated_time;
    int user_id;
    int post_centiment_score;
}
