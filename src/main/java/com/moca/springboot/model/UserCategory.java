package com.moca.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "user")
public class UserCategory {

    @Id
    private String categoryName;
    private int categoryPriority;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


}