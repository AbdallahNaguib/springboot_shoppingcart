package com.example.shoppingcart.error.handler;

import com.example.shoppingcart.error.AppError;
import com.example.shoppingcart.error.UserNotAdminException;
import com.example.shoppingcart.error.UsernameAlreadyUsedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class UserExceptionsController {
    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<?> handle(UsernameAlreadyUsedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new AppError("user not created", "username taken"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handle(EntityNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserNotAdminException.class)
    public ResponseEntity<?> handle(UserNotAdminException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
