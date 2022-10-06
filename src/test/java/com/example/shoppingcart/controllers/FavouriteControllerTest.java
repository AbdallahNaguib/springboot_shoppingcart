package com.example.shoppingcart.controllers;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.controller.FavouriteController;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.FavouriteDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.repository.ProductRepo;
import com.example.shoppingcart.repository.UserRepo;
import com.example.shoppingcart.services.SecurityContextService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class FavouriteControllerTest {
    @Autowired
    private FavouriteController favouriteController;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SecurityContextService securityContextService;
    private AppUser customer,customer2;
    private Product product1,product2,product3;

    @BeforeEach
    @Transactional
    void setup() {
        product1 = Product.builder().build();
        product2 = Product.builder().build();
        product3 = Product.builder().build();
        UserInfo customerUserInfo = UserInfo.builder().usertype(Constants.CUSTOMER).build();
        customer = AppUser.builder().userInfo(customerUserInfo).build();
        customer2 = AppUser.builder().userInfo(customerUserInfo).build();
        customer = userRepo.save(customer);
        customer2 = userRepo.save(customer2);
        product1 = productRepo.save(product1);
        product2 = productRepo.save(product2);
        product3 = productRepo.save(product3);
    }

    @Test
    public void testFavouriteCRUD() {
        // save customer favourite list = [1,2]
        securityContextService.setAuthenticatedUser(customer);
        ResponseEntity<?> response = favouriteController.saveFavourite(product1.getId());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        response = favouriteController.saveFavourite(product2.getId());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        // save customer2 favourite list = [1,3]
        securityContextService.setAuthenticatedUser(customer2);
        response = favouriteController.saveFavourite(product1.getId());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        response = favouriteController.saveFavourite(product3.getId());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        securityContextService.setAuthenticatedUser(customer);
        List<ProductDTO.Retrieve> products = favouriteController.findAllFavourites().getBody();
    }
}
