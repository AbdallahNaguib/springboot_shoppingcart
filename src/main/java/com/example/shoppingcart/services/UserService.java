package com.example.shoppingcart.services;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.error.UsernameAlreadyUsedException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.UserDTO;
import com.example.shoppingcart.repository.UserInfoRepo;
import com.example.shoppingcart.repository.UserRepo;
import com.example.shoppingcart.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    private final PasswordEncoder bcryptEncoder;
    private final UserRepo userRepo;
    private final UserInfoRepo userInfoRepo;

    @Override
    public AppUser loadUserByUsername(String username) {
        List<AppUser> result = userRepo.findAppUserByUsername(username);
        if (!result.isEmpty()) {
            return result.get(0);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    @Override
    public AppUser createCustomer(UserDTO.Create user) {
        return createUser(user, Constants.CUSTOMER);
    }

    @Override
    public AppUser createAdmin(UserDTO.Create user) {
        return createUser(user, Constants.ADMIN);
    }

    private AppUser createUser(UserDTO.Create user, String usertype) {
        // check for other users with same username
        List<AppUser> result = userRepo.findAppUserByUsername(user.getUsername());
        if (!result.isEmpty()) {
            throw new UsernameAlreadyUsedException();
        }
        UserInfo userInfo = UserInfo.builder()
                .usertype(usertype)
                .username(user.getUsername())
                .password(bcryptEncoder.encode(user.getPassword()))
                .build();
        AppUser appUser = AppUser.builder()
                .userInfo(userInfo)
                .build();
        userInfo.setUser(appUser);

        userInfoRepo.save(userInfo);
        return userRepo.save(appUser);
    }
}
