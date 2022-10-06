package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO.Retrieve> findByCategory(Long categoryId);

    Product getProductIfExistsById(Long productId, String errorMsg);

    void save(ProductDTO.Create product);

    void deleteProduct(Long id);

    ProductDTO.Retrieve findProductById(Long productId, String errorMsg);
}
