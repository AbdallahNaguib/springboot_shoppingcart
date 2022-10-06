package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    CategoryDTO.Retrieve save(CategoryDTO.Create category, AppUser owner);

    List<CategoryDTO.Retrieve> findAll();

    void deleteCategoryById(Long id);

    void checkCategoryIsOwnedByAuthenticatedUser(Long id, String msg);

    CategoryDTO.Retrieve findById(Long id);

    void updateCategory(Long id, CategoryDTO.Update category);
}
