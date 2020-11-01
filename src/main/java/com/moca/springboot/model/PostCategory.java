package com.moca.springboot.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class PostCategory {
    @Id
    int post_id;
    String category_name;
}
