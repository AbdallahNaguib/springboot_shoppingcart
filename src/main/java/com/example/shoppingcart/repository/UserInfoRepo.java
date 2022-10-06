package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo,Long> {
}
