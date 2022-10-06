package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.services.interfaces.ICategoryService;
import com.example.shoppingcart.services.SecurityContextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class CategoryController {

    private final ICategoryService categoryService;
    private final SecurityContextService securityContextService;

    public CategoryController(ICategoryService categoryService, SecurityContextService securityContextService) {
        this.categoryService = categoryService;
        this.securityContextService = securityContextService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<?> save(@RequestBody CategoryDTO.Create category) {
        categoryService.save(category, securityContextService.getAuthenticatedUser());
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO.Retrieve>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO.Retrieve> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/admin/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO.Update category) {
        categoryService.updateCategory(id, category);
        return ResponseEntity.ok(null);
    }
}
