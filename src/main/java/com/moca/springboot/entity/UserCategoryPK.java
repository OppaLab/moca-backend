package com.moca.springboot.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCategoryPK implements Serializable {

    private String categoryName;
    private long user;
}
