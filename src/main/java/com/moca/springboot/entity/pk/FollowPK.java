package com.moca.springboot.entity.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowPK implements Serializable {
    private long user;
    private long followedUser;
}