package com.moca.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "user")
@IdClass(UserEntityPK.class)
public class UserEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Id
    private String entity;


    private int lfuCount;
}