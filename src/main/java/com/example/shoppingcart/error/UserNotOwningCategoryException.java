package com.example.shoppingcart.error;

public class UserNotOwningCategoryException extends RuntimeException {
    public UserNotOwningCategoryException(String error) {
        super(error);
    }
}
