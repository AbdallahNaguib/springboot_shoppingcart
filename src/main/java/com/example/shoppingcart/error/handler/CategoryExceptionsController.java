package com.example.shoppingcart.error.handler;

import com.example.shoppingcart.error.AppError;
import com.example.shoppingcart.error.CategoryNotFoundException;
import com.example.shoppingcart.error.UserNotOwningCategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategoryExceptionsController {
    @ExceptionHandler(UserNotOwningCategoryException.class)
    public ResponseEntity<?> handle(UserNotOwningCategoryException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AppError(e.getMessage(), "user doesn't own the category"));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handle(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError("",
                        "category not found"));
    }
}
