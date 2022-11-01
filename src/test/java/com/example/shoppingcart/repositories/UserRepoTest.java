package com.example.shoppingcart.repositories;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.UserDTO;
import com.example.shoppingcart.repository.UserInfoRepo;
import com.example.shoppingcart.repository.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserInfoRepo userInfoRepo;

    private void insertUser(String username, String usertype) {
        UserInfo userInfo = UserInfo.builder()
                .usertype(usertype)
                .username(username)
                .password("")
                .build();
        AppUser appUser = AppUser.builder()
                .userInfo(userInfo)
                .build();
        appUser = userRepo.save(appUser);
        userInfo.setUser(appUser);
        userInfoRepo.save(userInfo);
    }

    @Test
    public void testFindingUsersFindUsername() {
        userRepo.deleteAll();
        String username = "abc";
        String username2 = "abc2";
        String username3 = "abc3";
        insertUser(username, Constants.ADMIN);
        insertUser(username2, Constants.ADMIN);
        insertUser(username3, Constants.CUSTOMER);
        List<AppUser> user1List = userRepo.findAppUserByUsername(username);
        List<AppUser> user2List = userRepo.findAppUserByUsername(username2);
        List<AppUser> user3List = userRepo.findAppUserByUsername(username3);
        List<AppUser> emptyList = userRepo.findAppUserByUsername("any");
        Assertions.assertEquals(user3List.size(), 1);
        Assertions.assertEquals(user2List.size(), 1);
        Assertions.assertEquals(user1List.size(), 1);
        Assertions.assertEquals(emptyList.size(), 0);
        Assertions.assertEquals(user1List.get(0).getUsername(), username);
        Assertions.assertEquals(user2List.get(0).getUsername(), username2);
        Assertions.assertEquals(user3List.get(0).getUsername(), username3);
    }
}
