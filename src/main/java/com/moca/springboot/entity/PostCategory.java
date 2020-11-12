package com.moca.springboot.entity;

import com.moca.springboot.entity.pk.PostCategoryPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PostCategoryPK.class)
@ToString(exclude = "post")
public class PostCategory implements Serializable {
    @Id
    private String categoryName;

    @Id
    @JoinColumn(name = "postId")
    @ManyToOne
    private Post post;
}
