package com.moca.springboot.repository;

import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserEntity;
import com.moca.springboot.entity.pk.UserEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, UserEntityPK> {
    List<UserEntity> findByUser(User user);

    UserEntity findByUserAndEntity(User user, String entity);
}
