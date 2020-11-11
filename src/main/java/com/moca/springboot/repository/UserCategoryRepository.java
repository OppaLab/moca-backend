package com.moca.springboot.repository;

import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserCategory;
import com.moca.springboot.entity.pk.UserCategoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UserCategoryPK> {

    List<UserCategory> findByUser_UserId(long userId);

    List<UserCategory> findByUser(User user);
}
