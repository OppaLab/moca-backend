package com.moca.springboot.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostEntityPK implements Serializable {
    private long post;
    private String entity;
}
