package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.services.CategoryService;
import com.example.shoppingcart.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryGraphqlController {
    private final ICategoryService categoryService;

    @QueryMapping
    Iterable<CategoryDTO.Retrieve> categories() {
        return categoryService.findAll();
    }
}
