package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.services.CategoryService;
import com.example.shoppingcart.services.interfaces.ICategoryService;
import com.example.shoppingcart.services.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryGraphqlController {
    private final ICategoryService categoryService;
    private final IProductService productService;

    @QueryMapping
    Iterable<CategoryDTO.Retrieve> categories() {
        return categoryService.findAll();
    }

    @QueryMapping
    CategoryDTO.Retrieve categoryById(@Argument Long id){
        return categoryService.findById(id);
    }

    @MutationMapping
    long addProduct(@Argument ProductDTO.Create product){
        productService.save(product);
        return 100;
    }
}
