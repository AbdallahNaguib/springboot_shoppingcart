package com.example.shoppingcart.services;

import com.example.shoppingcart.error.ProductNotFoundException;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.repository.ProductRepo;
import com.example.shoppingcart.services.interfaces.ICategoryService;
import com.example.shoppingcart.services.interfaces.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepo productRepo;
    private final ICategoryService categoryService;

    public ProductService(ProductRepo productRepo, ICategoryService categoryService) {
        this.productRepo = productRepo;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductDTO.Retrieve> findByCategory(Long categoryId) {
        return null;
    }


    @Override
    public void save(ProductDTO.Create product) {
        Long categoryId = product.getCategoryId();
        CategoryDTO.Retrieve category = categoryService.findById(categoryId);
        categoryService.checkCategoryIsOwnedByAuthenticatedUser(categoryId, "product not saved");
        Product productEntity = product.mapToEntity();
        productEntity.setCategory(category.mapToEntity());
        productRepo.save(productEntity);
    }

    @Override
    public Product getProductIfExistsById(Long productId, String errorMsg) {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isEmpty())
            throw new ProductNotFoundException(errorMsg);
        return product.get();
    }

    @Override
    public void deleteProduct(Long id) {
        String errorMsg = "product not deleted";
        getProductIfExistsById(id, errorMsg);
        Long categoryId = productRepo.getCategoryIdFromProductId(id);
        categoryService.checkCategoryIsOwnedByAuthenticatedUser(categoryId, errorMsg);
        productRepo.deleteById(id);
    }

    @Override
    public ProductDTO.Retrieve findProductById(Long productId, String errorMsg) {
        return ProductDTO.Retrieve.mapFromEntity(getProductIfExistsById(productId, errorMsg));
    }
}
