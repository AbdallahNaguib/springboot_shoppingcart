package com.example.shoppingcart.services;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.error.ProductNotFoundException;
import com.example.shoppingcart.error.UserNotOwningCategoryException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.repository.ProductRepo;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private ProductService productService;
    AppUser admin;

    List<Product> mockProductsTable;

    Long currentProductId;

    @BeforeEach
    void before() {
        UserInfo adminUserInfo = UserInfo.builder().username("admin").usertype(Constants.ADMIN).build();
        admin = AppUser.builder().id(1L).userInfo(adminUserInfo).build();
        currentProductId = 1L;
        mockProductsTable = new ArrayList<>();
    }

    private void mockSavingInProductRepo() {
        when(productRepo.save(any()))
                .thenAnswer((Answer<Product>) par -> {
                    Product product = par.getArgument(0, Product.class);
                    product.setId(currentProductId++);
                    mockProductsTable.add(product);
                    return product;
                });
    }

    private void mockRetrievingCategory() {
        when(categoryService.findById(any()))
                .thenReturn(new CategoryDTO.Retrieve());
    }

    private void mockFindProductById() {
        doAnswer(par -> {
            Long prodId = par.getArgument(0, Long.class);
            return mockProductsTable.stream().filter(product -> product.getId().equals(prodId)).findFirst();
        }).when(productRepo).findById(any());
    }

    private void mockDeletingFromProductRepo() {
        doAnswer(par -> {
            Long prodId = par.getArgument(0, Long.class);
            mockProductsTable.removeIf(product -> product.getId().equals(prodId));
            return null;
        }).when(productRepo).deleteById(any());
    }

    private void mockValidUserAuthenticated() {
        doAnswer(par -> null).when(categoryService).checkCategoryIsOwnedByAuthenticatedUser(any(), any());
    }


    private void mockInvalidUserAuthenticated() {
        doAnswer(par -> {
            throw new UserNotOwningCategoryException("");
        }).when(categoryService).checkCategoryIsOwnedByAuthenticatedUser(any(), any());
    }

    @Test
    public void testSavingProductWithValidUser() {
        mockSavingInProductRepo();
        mockRetrievingCategory();
        mockValidUserAuthenticated();

        ProductDTO.Create product1 = new ProductDTO.Create();
        ProductDTO.Create product2 = new ProductDTO.Create();
        productService.save(product1);
        productService.save(product2);

        Assertions.assertEquals(mockProductsTable.size(), 2);
    }


    @Test
    public void testSavingProductWithInValidUser() {
        mockRetrievingCategory();
        mockInvalidUserAuthenticated();
        ProductDTO.Create product1 = new ProductDTO.Create();
        Assertions.assertThrows(UserNotOwningCategoryException.class, () -> productService.save(product1));

    }

    @Test
    public void testDeletingProductWithValidUser() {
        mockValidUserAuthenticated();
        mockDeletingFromProductRepo();
        mockFindProductById();

        Category category = Category.builder().id(1L).name("cat1").user(admin).build();
        Product product = Product.builder()
                .id(1L)
                .name("p1")
                .category(category)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("p2")
                .category(category)
                .build();

        mockProductsTable.add(product);
        mockProductsTable.add(product2);

        productService.deleteProduct(1L);

        Assertions.assertEquals(mockProductsTable.size(), 1L);
        Assertions.assertEquals(mockProductsTable.get(0).getId(), 2L);
    }

    @Test
    public void testDeletingProductWithInValidUser() {
        mockInvalidUserAuthenticated();
        mockFindProductById();

        Category category = Category.builder().id(1L).name("cat1").user(admin).build();
        Product product = Product.builder()
                .id(1L)
                .name("p1")
                .category(category)
                .build();


        mockProductsTable.add(product);

        Assertions.assertThrows(UserNotOwningCategoryException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    public void testDeletingInvalidProduct() {
        mockFindProductById();

        Category category = Category.builder().id(1L).name("cat1").user(admin).build();
        Product product = Product.builder()
                .id(1L)
                .name("p1")
                .category(category)
                .build();


        mockProductsTable.add(product);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(2L));
    }
}
