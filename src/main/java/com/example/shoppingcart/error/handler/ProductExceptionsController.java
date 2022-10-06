package com.example.shoppingcart.error.handler;

import com.example.shoppingcart.error.AppError;
import com.example.shoppingcart.error.CategoryNotFoundException;
import com.example.shoppingcart.error.ProductNotFoundException;
import com.example.shoppingcart.error.UserNotOwningCategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionsController {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handle(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(e.getMessage(),
                        "product not found"));
    }
}
