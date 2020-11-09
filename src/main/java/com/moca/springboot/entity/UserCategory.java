package com.moca.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "user")
@IdClass(UserCategoryPK.class)
public class UserCategory implements Serializable {

    @Id
    private String categoryName;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}