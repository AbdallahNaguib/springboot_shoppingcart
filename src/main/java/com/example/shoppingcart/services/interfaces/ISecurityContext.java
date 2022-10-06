package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.AppUser;

public interface ISecurityContext {
    void setAuthenticatedUser(AppUser user);
    AppUser getAuthenticatedUser();
}
