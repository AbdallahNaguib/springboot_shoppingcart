package com.example.shoppingcart.repositories;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.CategoryRepo;
import com.example.shoppingcart.repository.ProductRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

@SpringBootTest
public class ProductRepoTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    public void addProductsAndRetrieveByCategoryId() {
        Category cat1 = new Category("cat1", null);
        Category cat2 = new Category("cat1", null);
        Long cat1Id = categoryRepo.save(cat1).getId();
        Long cat2Id = categoryRepo.save(cat2).getId();
        Product prod1 = Product.builder().name("prod1").category(cat1).build();
        Product prod2 = Product.builder().name("prod2").category(cat1).build();
        Product prod3 = Product.builder().name("prod3").category(cat2).build();
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        List<Product> products = productRepo.findProductByCategory_Id(cat1Id);
        Assertions.assertEquals(products.size(), 2);
        products = productRepo.findProductByCategory_Id(cat2Id);
        Assertions.assertEquals(products.size(), 1);
    }

    @Test
    public void testRetrievingCategoryIdFromProductId() {
        Category category = Category.builder().build();
        Category category2 = Category.builder().build();
        Long cat1Id = categoryRepo.save(category).getId();
        Long cat2Id = categoryRepo.save(category2).getId();
        Product prod1 = Product.builder().category(category).build();
        Product prod2 = Product.builder().category(category2).build();
        Long prod1Id = productRepo.save(prod1).getId();
        Long prod2Id = productRepo.save(prod2).getId();
        Assertions.assertEquals(productRepo.getCategoryIdFromProductId(prod1Id),cat1Id);
        Assertions.assertEquals(productRepo.getCategoryIdFromProductId(prod2Id),cat2Id);
    }
}
