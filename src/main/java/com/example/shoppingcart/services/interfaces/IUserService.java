package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.dto.UserDTO;

public interface IUserService {
    AppUser createCustomer(UserDTO.Create user);
    AppUser createAdmin(UserDTO.Create user);
}
