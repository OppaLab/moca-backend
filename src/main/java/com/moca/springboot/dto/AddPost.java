package com.moca.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddPost {
    String thumbnailImage;
    String postTitle;
    String postBody;
    long userId;
    List<String> postCategories;
}
