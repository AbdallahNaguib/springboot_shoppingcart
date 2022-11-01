package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<AppUser,Long> {
    @Query(nativeQuery = true,value = "select users.* from users left join user_info on " +
            "users.user_info_id = user_info.id where user_info.username = :username")
    List<AppUser> findAppUserByUsername(@Param("username") String username);
// solve this query issue
}
