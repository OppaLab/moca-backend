package com.moca.springboot.entity;

import com.moca.springboot.entity.pk.FollowPK;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@IdClass(FollowPK.class)
public class Follow {
    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    @NonNull
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "followedUserId")
    @NonNull
    private User followedUser;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
}
