package com.moca.springboot.model;

import lombok.NoArgsConstructor;
        import lombok.AllArgsConstructor;
        import lombok.Setter;
        import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long comment_id;
    @Column(nullable = false, unique = true)
    private long post_id;
    @Column(nullable = false, unique = false)
    private long review_id;
    @Column(nullable = true, unique = false)
    private long user_id;
    @Column(nullable = false, unique = false)
    private String text;
    private LocalDateTime created_at;


}

