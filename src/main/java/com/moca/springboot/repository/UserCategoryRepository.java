package com.moca.springboot.repository;

import com.moca.springboot.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, String> {

}
