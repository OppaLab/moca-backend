package com.moca.springboot.entity.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEntityPK implements Serializable {
    private long user;
    private String entity;
}
