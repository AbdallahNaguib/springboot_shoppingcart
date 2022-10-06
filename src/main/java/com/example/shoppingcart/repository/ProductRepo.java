package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findProductByCategory_Id(Long categoryId);

    @Query(value = "select products.category_id from products where products.id = :productId",nativeQuery = true)
    Long getCategoryIdFromProductId(@Param("productId") Long productId);
}
