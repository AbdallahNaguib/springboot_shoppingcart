package com.example.shoppingcart.services;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.error.UsernameAlreadyUsedException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.repository.UserRepo;
import com.example.shoppingcart.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService , IUserService {

    private final PasswordEncoder bcryptEncoder;

    private final UserRepo userRepo;

    public UserService(PasswordEncoder bcryptEncoder, UserRepo userRepo) {
        this.bcryptEncoder = bcryptEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        AppUser user = userRepo.findAppUserByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    @Override
    public AppUser createCustomer(AppUser user) {
        user.getUserInfo().setUsertype(Constants.CUSTOMER);
        return createUser(user);
    }

    @Override
    public AppUser createAdmin(AppUser user) {
        user.getUserInfo().setUsertype(Constants.ADMIN);
        return createUser(user);
    }

    private AppUser createUser(AppUser user) {
        // check for other users with same username
        AppUser otherUserWithSameName = userRepo.findAppUserByUsername(user.getUsername());
        if (otherUserWithSameName != null) {
            throw new UsernameAlreadyUsedException();
        }
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
