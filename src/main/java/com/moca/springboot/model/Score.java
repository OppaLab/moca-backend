package com.moca.springboot.model;

import lombok.Data;

@Data
public class Score {
    private long userId;
    private long postId;
    private float categoryScore;
    private float entityScore;
    private float recencyScore;
    private float popularityScore;
    private float sentimentScore;

    public Score() {
        this.categoryScore = 0;
        this.entityScore = 0;
        this.recencyScore = 0;
        this.popularityScore = 0;
        this.sentimentScore = 0;
    }
}
