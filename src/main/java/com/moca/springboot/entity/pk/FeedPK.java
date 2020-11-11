package com.moca.springboot.entity.pk;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeedPK implements Serializable {
    private long user;
    private long post;
}
