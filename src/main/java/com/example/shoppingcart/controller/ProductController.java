package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.services.ProductService;
import com.example.shoppingcart.services.interfaces.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/product")
    public ResponseEntity<?> save(@RequestBody ProductDTO.Create product) {
        productService.save(product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/product/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
