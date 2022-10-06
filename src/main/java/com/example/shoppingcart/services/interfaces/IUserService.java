package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.AppUser;

public interface IUserService {
    AppUser createCustomer(AppUser user);
    AppUser createAdmin(AppUser user);
}
