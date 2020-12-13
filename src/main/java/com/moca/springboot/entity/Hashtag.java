package com.moca.springboot.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Hashtag {
    @Id
    int post_id;
    String hashtag_name;
}
