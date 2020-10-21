package com.moca.springboot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long like_id;
    private long post_id;
    private long review_id;
    private long user_id;
    private LocalDateTime created_at;

}
