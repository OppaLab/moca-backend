package com.moca.springboot.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDTO {
    String thumbnailImageFilePathName;
    String postTitle;
    String postBody;
    long userId;
    List<String> postCategories;
}
