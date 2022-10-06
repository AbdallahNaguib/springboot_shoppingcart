package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);

}
