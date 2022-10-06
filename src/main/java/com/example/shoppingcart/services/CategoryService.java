package com.example.shoppingcart.services;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.error.CategoryNotFoundException;
import com.example.shoppingcart.error.UserNotAdminException;
import com.example.shoppingcart.error.UserNotOwningCategoryException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.repository.CategoryRepo;
import com.example.shoppingcart.services.interfaces.ICategoryService;
import com.example.shoppingcart.services.interfaces.ISecurityContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;
    private final ISecurityContext securityContextService;

    public CategoryService(CategoryRepo categoryRepo, ISecurityContext securityContextService) {
        this.categoryRepo = categoryRepo;
        this.securityContextService = securityContextService;
    }

    @Override
    public CategoryDTO.Retrieve save(CategoryDTO.Create category, AppUser owner) {
        Category entity = category.mapToEntity();
        if (!owner.getUsertype().equals(Constants.ADMIN)) {
            throw new UserNotAdminException();
        }
        entity.setUser(owner);
        Category savedCategory = categoryRepo.save(entity);
        return CategoryDTO.Retrieve.mapFromEntity(savedCategory);
    }

    @Override
    public List<CategoryDTO.Retrieve> findAll() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream().map(CategoryDTO.Retrieve::mapFromEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteCategoryById(Long id) {
        checkCategoryIsOwnedByAuthenticatedUser(id, "category not deleted");
        // if the category id doesn't exist then getReferenceById will throw
        // EntityNotFoundException which is handled in UserExceptionsController
        Category category = categoryRepo.getReferenceById(id);
        categoryRepo.delete(category);
    }

    @Override
    public void checkCategoryIsOwnedByAuthenticatedUser(Long id, String errorMsg) {
        Category category = categoryRepo.getReferenceById(id);
        Long categoryOwnerId = category.getUser().getId();
        Long authenticatedUserId = securityContextService.getAuthenticatedUser().getId();
        if (!categoryOwnerId.equals(authenticatedUserId)) {
            throw new UserNotOwningCategoryException(errorMsg);
        }
    }

    @Override
    public CategoryDTO.Retrieve findById(Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return CategoryDTO.Retrieve.mapFromEntity(categoryRepo.getReferenceById(id));
    }

    @Override
    public void updateCategory(Long id, CategoryDTO.Update category) {
        checkCategoryIsOwnedByAuthenticatedUser(id, "category not updated");
        Category categoryEntity = categoryRepo.getReferenceById(id);
        categoryEntity.setName(category.getName());
        categoryRepo.save(categoryEntity);
    }
}
