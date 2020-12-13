package com.moca.springboot.entity.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostCategoryPK implements Serializable {

    private long post;
    private String categoryName;
}