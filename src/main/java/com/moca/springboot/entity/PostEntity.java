package com.moca.springboot.entity;


import com.moca.springboot.entity.pk.PostEntityPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "post")
@IdClass(PostEntityPK.class)
public class PostEntity {

    @Id
    private String entity;

    @Id
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
}
