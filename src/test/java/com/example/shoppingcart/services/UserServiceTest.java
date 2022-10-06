package com.example.shoppingcart.services;

import com.example.shoppingcart.error.UsernameAlreadyUsedException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.UserDTO;
import com.example.shoppingcart.repository.UserInfoRepo;
import com.example.shoppingcart.repository.UserRepo;
import com.example.shoppingcart.services.interfaces.IUserService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private PasswordEncoder bcryptEncoder;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserInfoRepo userInfoRepo;
    private List<AppUser> appUsers;
    private List<UserInfo> userDetails;

    @BeforeEach
    public void init() {
        userDetails = new ArrayList<>();
        appUsers = new ArrayList<>();
    }

    private void mock() {
        when(userRepo.save(any())).thenAnswer((Answer<AppUser>) par -> {
            AppUser user = par.getArgument(0, AppUser.class);
            AppUser userCopy = AppUser.builder()
                    .userInfo(user.getUserInfo())
                    .build();
            appUsers.add(userCopy);
            return userCopy;
        });
        when(userInfoRepo.save(any())).thenAnswer((Answer<UserInfo>) par -> {
            UserInfo user = par.getArgument(0, UserInfo.class);
            UserInfo userCopy = UserInfo.builder()
                    .usertype(user.getUsertype())
                    .password(user.getPassword())
                    .username(user.getUsername())
                    .id(user.getId())
                    .build();
            userDetails.add(userCopy);
            return userCopy;
        });
        when(userRepo.findAppUserByUsername(any()))
                .thenAnswer((Answer<List<AppUser>>) par -> {
                    String username = par.getArgument(0, String.class);
                    return appUsers.stream().filter(user -> user.getUsername().equals(username)).toList();
                });
        when(bcryptEncoder.encode(any())).thenAnswer((Answer<String>) par -> {
            String input = par.getArgument(0, String.class);
            return input + input;
        });
    }

    @Test
    public void testInsertingUsers() {
        mock();
        String username1 = "customer1";
        String username2 = "admin1";
        String password1 = "pass1";
        String password2 = "pass2";
        userService.createCustomer(UserDTO.Create.builder()
                .username(username1)
                .password(password1)
                .build());

        userService.createAdmin(UserDTO.Create.builder()
                .username(username2)
                .password(password2)
                .build());

        Assertions.assertEquals(userDetails.size(), 2);
        Assertions.assertEquals(appUsers.size(), 2);
        Assertions.assertEquals(appUsers.get(0).getUsername(), username1);
        Assertions.assertEquals(appUsers.get(1).getUsername(), username2);

        Assertions.assertEquals(userDetails.get(0).getUsername(), username1);
        Assertions.assertEquals(userDetails.get(1).getUsername(), username2);

        // check mocked password encryption method
        Assertions.assertEquals(userDetails.get(0).getPassword(), password1 + password1);
        Assertions.assertEquals(userDetails.get(1).getPassword(), password2 + password2);

        Assertions.assertThrows(UsernameAlreadyUsedException.class, () -> {
            userService.createCustomer(UserDTO.Create.builder()
                    .username(username1)
                    .password("")
                    .build());
        });
    }
}
