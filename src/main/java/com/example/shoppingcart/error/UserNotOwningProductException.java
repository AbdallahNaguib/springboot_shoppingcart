package com.example.shoppingcart.error;

public class UserNotOwningProductException extends RuntimeException {
    public UserNotOwningProductException(String error) {
        super(error);
    }
}
